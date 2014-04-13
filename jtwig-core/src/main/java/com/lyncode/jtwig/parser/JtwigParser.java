/**
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
import com.lyncode.jtwig.parser.addons.JtwigContentAddon;
import com.lyncode.jtwig.parser.addons.JtwigContentAddonParser;
import com.lyncode.jtwig.parser.addons.JtwigEmptyContentAddon;
import com.lyncode.jtwig.parser.addons.JtwigEmptyContentAddonParser;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.tree.api.Content;
import com.lyncode.jtwig.tree.content.*;
import com.lyncode.jtwig.tree.documents.JtwigDocument;
import com.lyncode.jtwig.tree.documents.JtwigExtendsDocument;
import com.lyncode.jtwig.tree.documents.JtwigRootDocument;
import com.lyncode.jtwig.tree.expressions.Constant;
import com.lyncode.jtwig.tree.expressions.FunctionElement;
import com.lyncode.jtwig.tree.expressions.Variable;
import com.lyncode.jtwig.tree.structural.Block;
import com.lyncode.jtwig.tree.structural.Extends;
import com.lyncode.jtwig.tree.structural.Include;
import com.lyncode.jtwig.tree.tags.Filter;
import com.lyncode.jtwig.tree.tags.Verbatim;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.common.FileUtils;
import org.parboiled.errors.ParserRuntimeException;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.lyncode.jtwig.parser.JtwigKeyword.*;
import static com.lyncode.jtwig.parser.JtwigSymbol.ATTR;
import static com.lyncode.jtwig.parser.JtwigSymbol.COMMA;
import static com.lyncode.jtwig.tree.expressions.Operator.COMPOSITION;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.parboiled.Parboiled.createParser;

public class JtwigParser extends JtwigBaseParser<Content> {
    public static JtwigParser newParser(
            JtwigResource resource,
            ParserConfiguration configuration,
            List<Class<? extends JtwigEmptyContentAddonParser>> emptyAddons,
            List<Class<? extends JtwigContentAddonParser>> contentAddons

    ) {
        return createParser(JtwigParser.class, resource, configuration, emptyAddons, contentAddons);
    }

    public static JtwigDocument parse(JtwigParser parser, JtwigResource input) throws ParseException {
        try {
            ReportingParseRunner<Object> runner = new ReportingParseRunner<Object>(parser.start());
            ParsingResult<Object> result = runner.run(
                    FileUtils.readAllText(input.retrieve(), Charset.defaultCharset()));
            return (JtwigDocument) result.resultValue;
        } catch (ParserRuntimeException e) {
            if (e.getCause() instanceof ParseBypassException) {
                ParseException innerException = ((ParseBypassException) e.getCause()).getInnerException();
                innerException.setExpression(e.getMessage());
                throw innerException;
            } else {
                throw new ParseException(e);
            }
        } catch (ResourceException e) {
            throw new ParseException(e);
        }
    }

    final JtwigBasicParser basicParser;
    final JtwigExpressionParser expressionParser;
    final JtwigTagPropertyParser tagPropertyParser;

    JtwigEmptyContentAddonParser[] noContentAddonParsers;
    JtwigContentAddonParser[] contentAddonParsers;
    List<Class<? extends BaseParser>> emptyAddons;
    List<Class<? extends BaseParser>> contentAddons;
    ParserConfiguration configuration;

    public JtwigParser(JtwigResource resource, ParserConfiguration configuration, List<Class<? extends BaseParser>> emptyAddons, List<Class<? extends BaseParser>> contentAddons) {
        super(resource);
        basicParser = createParser(JtwigBasicParser.class, configuration);
        tagPropertyParser = createParser(JtwigTagPropertyParser.class, configuration);
        expressionParser = createParser(JtwigExpressionParser.class, resource, configuration);

        this.emptyAddons = emptyAddons;
        this.contentAddons = contentAddons;
        this.configuration = configuration;

        noContentAddonParsers = new JtwigEmptyContentAddonParser[emptyAddons.size()];
        contentAddonParsers = new JtwigContentAddonParser[contentAddons.size()];

        for (int i = 0; i < emptyAddons.size(); i++) {
            noContentAddonParsers[i] = (JtwigEmptyContentAddonParser) createParser(emptyAddons.get(i), resource, configuration);
        }

        for (int i = 0; i < contentAddons.size(); i++) {
            contentAddonParsers[i] = (JtwigContentAddonParser) createParser(contentAddons.get(i), resource, configuration);
        }
    }

    public JtwigParser clone (JtwigResource resource) {
        return createParser(JtwigParser.class, resource, configuration, emptyAddons, contentAddons);
    }

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
                        basicParser.openCode(),
                        basicParser.spacing(),
                        keyword(EXTENDS),
                        mandatory(
                                Sequence(
                                        basicParser.stringLiteral(),
                                        basicParser.spacing(),
                                        basicParser.closeCode(),
                                        push(new JtwigExtendsDocument(new Extends(basicParser.pop()))),
                                        ZeroOrMore(
                                                basicParser.spacing(),
                                                block(),
                                                action(peek(1, JtwigExtendsDocument.class).add(pop(Block.class)))
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
                                addToContent(embed()),
                                addToContent(filter()),
                                addToContent(forEach()),
                                addToContent(ifCondition()),
                                addToContent(set()),
                                addToContent(verbatim()),
                                addToContent(comment()),
                                addToContent(emptyContentParsers()),
                                addToContent(contentParsers()),
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
                                                        keyword(ELSEIF),
                                                        keyword(ELSE),
                                                        keyword(VERBATIM),
                                                        keyword(ENDFILTER),
                                                        keywordEmptyContent(),
                                                        keywordsContent()
                                                )
                                        ),
                                        throwException(new ParseException("Unknown tag"))
                                ),
                                addToContent(text())
                        )
                )
        );
    }

    Rule keywordsContent() {
        if (contentAddonParsers.length == 0) {
            return Test(false);
        }
        Rule[] rules = new Rule[contentAddonParsers.length];
        for (int i = 0; i < contentAddonParsers.length; i++) {
            rules[i] = FirstOf(
                    basicParser.terminal(contentAddonParsers[i].beginKeyword()),
                    basicParser.terminal(contentAddonParsers[i].endKeyword())
            );
        }
        return FirstOf(rules);
    }

    Rule contentParsers() {
        if (contentAddonParsers.length == 0) {
            return Test(false);
        }
        Rule[] rules = new Rule[contentAddonParsers.length];
        for (int i = 0; i < contentAddonParsers.length; i++) {
            rules[i] = contentAddon(contentAddonParsers[i]);
        }
        return FirstOf(rules);
    }

    Rule contentAddon(JtwigContentAddonParser parser) {
        return Sequence(
                openCode(),
                basicParser.terminal(parser.beginKeyword()),
                basicParser.spacing(),
                parser.startRule(),
                mandatory(
                        Test(instanceOf(JtwigContentAddon.class).matches(peek())),
                        new ParseException(
                                "Addon parser not pushing a JtwigContentAddon object to the top of the stack")
                ),
                mandatory(
                        Sequence(
                                action(peek(JtwigContentAddon.class).begin().addToLeft(tagPropertyParser.getCurrentProperty())),
                                closeCode(),
                                action(peek(JtwigContentAddon.class).begin().addToRight(tagPropertyParser.getCurrentProperty())),
                                content(),
                                action(peek(1, JtwigContentAddon.class).setContent(pop(JtwigContent.class))),
                                openCode(),
                                basicParser.terminal(parser.endKeyword()),
                                basicParser.spacing(),
                                action(peek(JtwigContentAddon.class).end().addToLeft(tagPropertyParser.getCurrentProperty())),
                                closeCode(),
                                action(peek(JtwigContentAddon.class).end().addToRight(tagPropertyParser.getCurrentProperty()))
                        ),
                        new ParseException("Wrong syntax for " + parser.beginKeyword())
                )
        );
    }

    Rule keywordEmptyContent() {
        if (noContentAddonParsers.length == 0) {
            return Test(false);
        }
        Rule[] rules = new Rule[noContentAddonParsers.length];
        for (int i = 0; i < noContentAddonParsers.length; i++) {
            rules[i] = basicParser.terminal(noContentAddonParsers[i].keyword());
        }
        return FirstOf(rules);
    }

    Rule emptyContentParsers() {
        List<Rule> rules = new ArrayList<>();
//        if (noContentAddonParsers.isEmpty())
        return Test(false);
//        for (JtwigEmptyContentAddonParser parser : noContentAddonParsers)
//            rules.add(noContentAddon(parser));
//        return FirstOf(rules.toArray(new Rule[rules.size()]));
    }

    Rule noContentAddon(JtwigEmptyContentAddonParser parser) {
        return Sequence(
                openCode(),
                basicParser.terminal(parser.keyword()),
                basicParser.spacing(),
                parser.rule(),
                mandatory(
                        Test(instanceOf(JtwigEmptyContentAddon.class).matches(peek())),
                        new ParseException(
                                "Addon parser not pushing a JtwigEmptyContent object to the top of the stack")
                ),
                mandatory(
                        Sequence(
                                action(peek(JtwigEmptyContentAddon.class).begin().addToLeft(tagPropertyParser.getCurrentProperty())),
                                closeCode(),
                                action(peek(JtwigEmptyContentAddon.class).end().addToLeft(tagPropertyParser.getCurrentProperty())
                                )
                        ),
                        new ParseException("Wrong syntax for " + parser.keyword())
                )
        );
    }

    Rule addToContent(Rule innerRule) {
        return Sequence(
                innerRule,
                action(peek(1, JtwigContent.class).add(pop()))
        );
    }

    Rule block() {
        return Sequence(
                openCode(),
                keyword(BLOCK),
                mandatory(
                        Sequence(
                                expressionParser.variable(),
                                push(new Block(currentPosition(), expressionParser.pop(Variable.class).getIdentifier())),
                                action(peek(Block.class).begin().addToLeft(tagPropertyParser.getCurrentProperty())),
                                closeCode(),
                                action(peek(Block.class).begin().addToRight(tagPropertyParser.getCurrentProperty())),
                                content(),
                                action(peek(1, Block.class).setContent(pop(JtwigContent.class))),
                                openCode(),
                                action(peek(Block.class).end().addToLeft(tagPropertyParser.getCurrentProperty())),
                                keyword(ENDBLOCK),
                                Optional(
                                        expressionParser.variable(),
                                        assertEqual(
                                                peek(Block.class).getName(),
                                                expressionParser.pop(Variable.class).getIdentifier()
                                        )
                                ),
                                closeCode(),
                                action(peek(Block.class).end().addToRight(tagPropertyParser.getCurrentProperty()))
                        ),
                        new ParseException("Wrong block syntax")
                )
        );
    }

    boolean assertEqual(String value1, String value2) {
        if (!value1.equals(value2)) {
            return throwException(new ParseException("Start statement and ending block names do not match"));
        } else {
            return true;
        }
    }

    Rule include() {
        return Sequence(
                openCode(),
                keyword(INCLUDE),
                mandatory(
                        Sequence(
                                basicParser.stringLiteral(),
                                basicParser.spacing(),
                                push(new Include(currentPosition(), basicParser.pop())),
                                action(peek(Include.class).begin().addToLeft(tagPropertyParser.getCurrentProperty())),
                                closeCode(),
                                action(peek(Include.class).end().addToRight(tagPropertyParser.getCurrentProperty()))
                        ),
                        new ParseException("Wrong include syntax")
                )
        );
    }

    Rule embed() {
        return Sequence(
                openCode(),
                keyword(EMBED),
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
                                openCode(),
                                keyword(ENDEMBED),
                                closeCode()
                        ),
                        new ParseException("Wrong embed syntax")
                )
        );
    }

    Rule text() {
        return Sequence(
                push(new Text()),
                OneOrMore(
                        FirstOf(
                                Sequence(
                                        basicParser.escape(),
                                        peek(Text.class).append(match())
                                ),
                                Sequence(
                                        TestNot(
                                                FirstOf(
                                                        basicParser.openCode(),
                                                        basicParser.openOutput(),
                                                        basicParser.openComment()
                                                )
                                        ),
                                        ANY,
                                        peek(Text.class).append(match())
                                )
                        )
                ).suppressSubnodes()
        );
    }

    Rule verbatim() {
        return Sequence(
                openCode(),
                keyword(VERBATIM),
                mandatory(
                        Sequence(
                                push(new Verbatim(currentPosition())),
                                action(peek(Verbatim.class).begin().addToLeft(tagPropertyParser.getCurrentProperty())),
                                closeCode(),
                                text(Sequence(
                                        basicParser.openCode(),
                                        basicParser.spacing()
                                        , keyword(ENDVERBATIM)
                                )),
                                action(peek(1, Verbatim.class).withText(pop(Text.class))),
                                openCode(),
                                keyword(JtwigKeyword.ENDVERBATIM),
                                closeCode(),
                                action(peek(Verbatim.class).end().addToRight(tagPropertyParser.getCurrentProperty()))
                        ),
                        new ParseException("Wrong verbatim syntax")
                )
        );
    }

    Rule text(Rule until) {
        return Sequence(
                push(new Text()),
                OneOrMore(
                        FirstOf(
                                Sequence(
                                        basicParser.escape(),
                                        peek(Text.class).append(match())
                                ),
                                Sequence(
                                        TestNot(
                                                until
                                        ),
                                        ANY,
                                        peek(Text.class).append(match())
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
                                push(new IfExpression(currentPosition(), new IfExpression.Case(currentPosition(), expressionParser.pop()))),
                                action(push(peek(IfExpression.class).current())),
                                action(peek(IfExpression.Case.class).begin().addToLeft(tagPropertyParser.getCurrentProperty())),
                                closeCode(),
                                action(peek(IfExpression.Case.class).begin().addToRight(tagPropertyParser.getCurrentProperty())),
                                content(),
                                action(peek(1, IfExpression.Case.class).setContent(pop(JtwigContent.class))),
                                ZeroOrMore(
                                        Sequence(
                                                openCode(),
                                                keyword(ELSEIF),
                                                expressionParser.expression(),
                                                push(new IfExpression.Case(currentPosition(), expressionParser.pop())),
                                                action(peek(1, IfExpression.Case.class).end().addToLeft(tagPropertyParser.getCurrentProperty())),
                                                action(peek(IfExpression.Case.class).begin().addToLeft(tagPropertyParser.getCurrentProperty())),
                                                closeCode(),
                                                action(peek(1, IfExpression.Case.class).end().addToRight(tagPropertyParser.getCurrentProperty())),
                                                action(peek(IfExpression.Case.class).begin().addToRight(tagPropertyParser.getCurrentProperty())),
                                                content(),
                                                action(peek(1, IfExpression.Case.class).setContent(pop(JtwigContent.class))),
                                                action(peek(2, IfExpression.class).add(peek(IfExpression.Case.class))),
                                                action(pop(1))
                                        )
                                ),
                                Optional(
                                        Sequence(
                                                openCode(),
                                                keyword(ELSE),
                                                push(new IfExpression.Case(currentPosition(), new Constant<>(true))),
                                                action(peek(1, IfExpression.Case.class).end().addToLeft(tagPropertyParser.getCurrentProperty())),
                                                action(peek(IfExpression.Case.class).begin().addToLeft(tagPropertyParser.getCurrentProperty())),
                                                closeCode(),
                                                action(peek(1, IfExpression.Case.class).end().addToLeft(tagPropertyParser.getCurrentProperty())),
                                                action(peek(IfExpression.Case.class).begin().addToRight(tagPropertyParser.getCurrentProperty())),
                                                content(),
                                                action(peek(1, IfExpression.Case.class).setContent(pop(JtwigContent.class))),
                                                action(peek(2, IfExpression.class).add(peek(IfExpression.Case.class))),
                                                action(pop(1))
                                        )
                                ),
                                openCode(),
                                action(peek(IfExpression.Case.class).end().addToLeft(tagPropertyParser.getCurrentProperty())),
                                keyword(ENDIF),
                                closeCode(),
                                action(peek(IfExpression.Case.class).end().addToRight(tagPropertyParser.getCurrentProperty())),
                                action(pop())
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
                                                symbolWithSpacing(COMMA),
                                                expressionParser.variable(),
                                                keyword(IN),
                                                expressionParser.expression(),
                                                push(new ForPairLoop(currentPosition(), expressionParser.pop(2, Variable.class),
                                                        expressionParser.pop(1, Variable.class),
                                                        expressionParser.pop()))
                                        ),
                                        Sequence(
                                                keyword(IN),
                                                expressionParser.expression(),
                                                push(new ForLoop(currentPosition(), expressionParser.pop(1, Variable.class),
                                                        expressionParser.pop()))
                                        )
                                ),
                                action(peek(ForLoop.class).begin().addToLeft(tagPropertyParser.getCurrentProperty())),
                                closeCode(),
                                action(peek(ForLoop.class).begin().addToRight(tagPropertyParser.getCurrentProperty())),
                                content(),
                                action(peek(1, ForLoop.class).setContent(pop(JtwigContent.class))),
                                openCode(),
                                action(peek(ForLoop.class).end().addToLeft(tagPropertyParser.getCurrentProperty())),
                                keyword(ENDFOR),
                                closeCode(),
                                action(peek(ForLoop.class).end().addToRight(tagPropertyParser.getCurrentProperty()))
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
                                push(new SetVariable(currentPosition(), expressionParser.pop(Variable.class))),
                                action(peek(SetVariable.class).begin().addToLeft(tagPropertyParser.getCurrentProperty())),
                                symbolWithSpacing(ATTR),
                                expressionParser.expression(),
                                action(peek(1, SetVariable.class).setAssignment(expressionParser.pop())),
                                closeCode(),
                                action(peek(SetVariable.class).end().addToRight(tagPropertyParser.getCurrentProperty()))
                        ),
                        new ParseException("Wrong set syntax")
                )
        );
    }

    Rule filter() {
        return Sequence(
                openCode(),
                keyword(FILTER),
                mandatory(
                        Sequence(
                                expressionParser.nonExpressionFunction(),
                                push(new Filter(currentPosition(), (FunctionElement) expressionParser.peek())),
                                swap(),
                                ZeroOrMore(
                                        Sequence(
                                                expressionParser.operator(COMPOSITION),
                                                action(expressionParser.pop()),
                                                expressionParser.nonExpressionFunction(),
                                                action(peek(FunctionElement.class).addArgument(0, expressionParser.pop(1))),
                                                action(peek(1, Filter.class).addExpression((FunctionElement) expressionParser.peek()))
                                        )
                                ),
                                action(expressionParser.pop()),
                                action(peek(Filter.class).begin().addToLeft(tagPropertyParser.getCurrentProperty())),
                                closeCode(),
                                action(peek(Filter.class).begin().addToRight(tagPropertyParser.getCurrentProperty())),
                                content(),
                                action(peek(1, Filter.class).setContent(pop(JtwigContent.class))),
                                openCode(),
                                action(peek(Filter.class).end().addToLeft(tagPropertyParser.getCurrentProperty())),
                                keyword(JtwigKeyword.ENDFILTER),
                                closeCode(),
                                action(peek(Filter.class).end().addToRight(tagPropertyParser.getCurrentProperty()))
                        ),
                        new ParseException("Wrong filter syntax")
                )
        );
    }

    Rule output() {
        return Sequence(
                basicParser.openOutput(),
                tagPropertyParser.property(),
                basicParser.spacing(),
                mandatory(
                        Sequence(
                                expressionParser.expression(),
                                push(new Output(currentPosition(), expressionParser.pop())),
                                action(peek(Output.class).begin().addToLeft(tagPropertyParser.getCurrentProperty())),
                                tagPropertyParser.property(),
                                action(peek(Output.class).end().addToRight(tagPropertyParser.getCurrentProperty())),
                                basicParser.closeOutput()
                        ),
                        new ParseException("Wrong output syntax")
                )
        );
    }

    Rule symbolWithSpacing(JtwigSymbol symbol) {
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

    Rule comment() {
        return Sequence(
                push(new Comment(currentPosition())),
                basicParser.openComment(),
                tagPropertyParser.property(),
                action(peek(Comment.class).begin().addToLeft(tagPropertyParser.getCurrentProperty())),
                ZeroOrMore(
                        TestNot(
                                Sequence(
                                        basicParser.symbol(JtwigSymbol.MINUS),
                                        basicParser.closeComment()
                                )
                        ),
                        TestNot(
                                basicParser.closeComment()
                        ),
                        ANY
                ),
                tagPropertyParser.property(),
                action(peek(Comment.class).end().addToRight(tagPropertyParser.getCurrentProperty())),
                basicParser.closeComment()
        );
    }

    Rule openCode() {
        return Sequence(
                basicParser.openCode(),
                tagPropertyParser.property(),
                basicParser.spacing()
        );
    }

    Rule closeCode() {
        return Sequence(
                tagPropertyParser.property(),
                basicParser.closeCode()
        );
    }

    Rule keyword(JtwigKeyword keyword) {
        return Sequence(
                basicParser.keyword(keyword),
                basicParser.spacing()
        );
    }
}
