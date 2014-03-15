/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.jtwig.parser;

import com.lyncode.jtwig.exception.ParseBypassException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.test.addons.spaceless.SpacelessParser;
import com.lyncode.jtwig.tree.api.Content;
import com.lyncode.jtwig.tree.content.*;
import com.lyncode.jtwig.tree.documents.JtwigDocument;
import com.lyncode.jtwig.tree.documents.JtwigExtendsDocument;
import com.lyncode.jtwig.tree.documents.JtwigRootDocument;
import com.lyncode.jtwig.tree.expressions.Variable;
import com.lyncode.jtwig.tree.structural.Block;
import com.lyncode.jtwig.tree.structural.Extends;
import com.lyncode.jtwig.tree.structural.Include;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.common.FileUtils;
import org.parboiled.errors.ParserRuntimeException;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;

import java.nio.charset.Charset;

import static com.lyncode.jtwig.parser.JtwigKeyword.*;
import static com.lyncode.jtwig.parser.JtwigSymbol.*;
import static org.parboiled.Parboiled.createParser;

public class JtwigParser extends BaseParser<Content> {
    public static JtwigDocument parse (JtwigResource input) throws ParseException {
        try {
            ReportingParseRunner<Object> runner = new ReportingParseRunner<Object>(createParser(JtwigParser.class).start());
            ParsingResult<Object> result = runner.run(FileUtils.readAllText(input.retrieve(), Charset.defaultCharset()));
            return (JtwigDocument) result.resultValue;
        } catch (ParserRuntimeException e) {
            if (e.getCause() instanceof ParseBypassException) {
                ParseException innerException = ((ParseBypassException) e.getCause()).getInnerException();
                innerException.setExpression(e.getMessage());
                throw innerException;
            } else
                throw new ParseException(e);
        } catch (ResourceException e) {
            throw new ParseException(e);
        }
    }

    JtwigBasicParser basicParser = createParser(JtwigBasicParser.class);
    JtwigExpressionParser expressionParser = createParser(JtwigExpressionParser.class);


    public Rule start() {
        return FirstOf(
                extendTemplate(),
                normalTemplate()
        );
    }

    Rule extendTemplate() {
        return Sequence(
                basicParser.spacing(),
                Sequence(
                        openCode(),
                        keyword(EXTENDS),
                        mandatory(
                                Sequence(
                                        basicParser.stringLiteral(),
                                        basicParser.spacing(),
                                        closeCode(),
                                        push(new JtwigExtendsDocument(new Extends(basicParser.pop()))),
                                        ZeroOrMore(
                                                basicParser.spacing(),
                                                block(),
                                                ((JtwigExtendsDocument) peek(1)).add((Block) pop())
                                        ),
                                        basicParser.spacing(),
                                        EOI
                                ),
                                new ParseException("Wrong include syntax")
                        )
                )
        );
    }

    Rule normalTemplate() {
        return Sequence(
                content(),
                push(new JtwigRootDocument(pop())),
                EOI
        );
    }

    Rule content() {
        return Sequence(
                push(new JtwigContent()),
                ZeroOrMore(
                        FirstOf(
                                addToContent(output()),
                                addToContent(block()),
                                addToContent(include()),
                                addToContent(forEach()),
                                addToContent(ifCondition()),
                                addToContent(set()),
                                addToContent(verbatim()),
                                Sequence(
                                        openCode(),
                                        TestNot(
                                                FirstOf(
                                                        keyword(ENDBLOCK),
                                                        keyword(ENDFOR),
                                                        keyword(ENDIF),
                                                        keyword(IF),
                                                        keyword(BLOCK),
                                                        keyword(FOR),
                                                        keyword(SET),
                                                        keyword(ELSE),
                                                        keyword(ELSEIF),
                                                        keyword(VERBATIM)
                                                )
                                        ),
                                        throwException(new ParseException("Unknown tag"))
                                ),
                                addToContent(text())
                        )
                )
        );
    }

    Rule addToContent(Rule innerRule) {
        return Sequence(
                innerRule,
                ((JtwigContent) peek(1)).add(pop())
        );
    }

    Rule block() {
        return Sequence(
                openCode(),
                keyword(BLOCK),
                mandatory(
                        Sequence(
                                expressionParser.variable(),
                                push(new Block(((Variable) expressionParser.pop()).getIdentifier())),
                                closeCode(),
                                content(),
                                (((Block) peek(1)).setContent(pop())),
                                openCode(),
                                keyword(ENDBLOCK),
                                Optional(
                                        expressionParser.variable(),
                                        assertEqual(
                                                ((Block)peek()).getName(),
                                                ((Variable) expressionParser.pop()).getIdentifier()
                                        )
                                ),
                                closeCode()
                        ),
                        new ParseException("Wrong block syntax")
                )
        );
    }

    boolean assertEqual(String value1, String value2) {
        if (!value1.equals(value2))
            return throwException(new ParseException("Start statement and ending block names do not match"));
        else
            return true;
    }

    Rule include() {
        return Sequence(
                openCode(),
                keyword(INCLUDE),
                mandatory(
                        Sequence(
                                basicParser.stringLiteral(),
                                basicParser.spacing(),
                                push(new Include(basicParser.pop())),
                                closeCode()
                        ),
                        new ParseException("Wrong include syntax")
                )
        );
    }

    Rule text() {
        return Sequence(
                push(new Text()),
                OneOrMore(
                        FirstOf(
                                Sequence("{#", ZeroOrMore(TestNot("#}"), ANY), "#}"),
                                Sequence(
                                        basicParser.escape(),
                                        ((Text) peek()).append(match())
                                ),
                                Sequence(
                                        TestNot(
                                                FirstOf(
                                                        basicParser.symbol(OPEN_OUTPUT),
                                                        basicParser.symbol(OPEN_CODE)
                                                )
                                        ),
                                        ANY,
                                        ((Text) peek()).append(match())
                                )
                        )
                ).suppressSubnodes()
        );
    }

    Rule verbatim () {
        return Sequence(
                openCode(),
                keyword(VERBATIM),
                mandatory(
                        Sequence(
                                closeCode(),
                                text(Sequence(openCode(), keyword(ENDVERBATIM))),
                                openCode(),
                                keyword(JtwigKeyword.ENDVERBATIM),
                                closeCode()
                        ),
                        new ParseException("Wrong verbatim syntax")
                )
        );
    }

    Rule text (Rule until) {
        return Sequence(
                push(new Text()),
                OneOrMore(
                        FirstOf(
                                Sequence(
                                        basicParser.escape(),
                                        ((Text) peek()).append(match())
                                ),
                                Sequence(
                                        TestNot(
                                                until
                                        ),
                                        ANY,
                                        ((Text) peek()).append(match())
                                )
                        )
                ).suppressSubnodes()
        );
    }

    Rule ifCondition() {
        return Sequence(
                openCode(),
                keyword(IF),
                mandatory(
                        Sequence(
                                expressionParser.expression(),
                                closeCode(),
                                push(new IfExpression(expressionParser.pop())),
                                content(),
                                ((IfExpression) peek(1)).setContent(pop()),
                                ZeroOrMore(
                                        Sequence(
                                                openCode(),
                                                keyword(ELSEIF),
                                                expressionParser.expression(),
                                                push(new IfExpression.ElseIfExpression(expressionParser.pop())),
                                                closeCode(),
                                                content(),
                                                ((IfExpression.ElseIfExpression) peek(1)).setContent(pop()),
                                                ((IfExpression) peek(1)).addElseIf((IfExpression.ElseIfExpression) pop())
                                        )
                                ),
                                Optional(
                                        Sequence(
                                                openCode(),
                                                keyword(ELSE),
                                                closeCode(),
                                                content(),
                                                ((IfExpression) peek(1)).setElseExpression(new IfExpression.ElseExpression((JtwigContent) pop()))
                                        )
                                ),
                                openCode(),
                                keyword(ENDIF),
                                closeCode()
                        ),
                        new ParseException("Wrong if syntax")
                )
        );
    }

    Rule forEach() {
        return Sequence(
                openCode(),
                keyword(FOR),
                mandatory(
                        Sequence(
                                expressionParser.variable(),
                                FirstOf(
                                        Sequence(
                                                symbol(COMMA),
                                                expressionParser.variable(),
                                                keyword(IN),
                                                expressionParser.expression(),
                                                push(new ForPairLoop((Variable) expressionParser.pop(2), (Variable) expressionParser.pop(1), expressionParser.pop()))
                                        ),
                                        Sequence(
                                                keyword(IN),
                                                expressionParser.expression(),
                                                push(new ForLoop((Variable) expressionParser.pop(1), expressionParser.pop()))
                                        )
                                ),
                                closeCode(),
                                content(),
                                ((ForLoop) peek(1)).setContent(pop()),
                                openCode(),
                                keyword(ENDFOR),
                                closeCode()
                        ),
                        new ParseException("Wrong for each syntax")
                )
        );
    }

    Rule set() {
        return Sequence(
                openCode(),
                keyword(SET),
                mandatory(
                        Sequence(
                                expressionParser.variable(),
                                push(new SetVariable((Variable) expressionParser.pop())),
                                symbol(ATTR),
                                expressionParser.expression(),
                                ((SetVariable) peek(1)).setAssignment(expressionParser.pop()),
                                closeCode()
                        ),
                        new ParseException("Wrong set syntax")
                )
        );
    }

    Rule output() {
        return Sequence(
                symbol(OPEN_OUTPUT),
                mandatory(
                        Sequence(
                                expressionParser.expression(),
                                push(new Output(expressionParser.pop())),
                                basicParser.symbol(JtwigSymbol.CLOSE_OUTPUT)
                        ),
                        new ParseException("Wrong output syntax")
                )
        );
    }

    Rule symbol(JtwigSymbol symbol) {
        return Sequence(
                basicParser.symbol(symbol),
                basicParser.spacing()
        );
    }


    Rule mandatory(Rule rule, ParseException exception) {
        return FirstOf(
                rule,
                throwException(exception)
        );
    }


    boolean throwException(ParseException exception) throws ParseBypassException {
        throw new ParseBypassException(exception);
    }

    Rule openCode() {
        return Sequence(
                basicParser.openCode(),
                basicParser.spacing()
        );
    }

    Rule keyword(JtwigKeyword keyword) {
        return Sequence(
                basicParser.keyword(keyword),
                basicParser.spacing()
        );
    }

    Rule closeCode() {
        return basicParser.closeCode();
    }

}
