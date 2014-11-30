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

package org.jtwig.parser.parboiled;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.jtwig.addons.Addon;
import org.jtwig.addons.AddonModel;
import org.jtwig.content.api.Compilable;
import org.jtwig.content.api.Tag;
import org.jtwig.content.model.compilable.*;
import org.jtwig.content.model.tag.WhiteSpaceControl;
import org.jtwig.exception.ParseBypassException;
import org.jtwig.exception.ParseException;
import org.jtwig.exception.ResourceException;
import org.jtwig.expressions.model.Constant;
import org.jtwig.expressions.model.Variable;
import org.jtwig.parser.config.ParserConfiguration;
import org.jtwig.parser.model.JtwigKeyword;
import org.jtwig.parser.model.JtwigSymbol;
import org.jtwig.resource.JtwigResource;
import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.common.FileUtils;
import org.parboiled.errors.ParserRuntimeException;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;

import javax.annotation.Nullable;
import java.nio.charset.Charset;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.jtwig.parser.model.JtwigKeyword.*;
import static org.jtwig.parser.model.JtwigSymbol.*;
import static org.jtwig.parser.model.JtwigTagProperty.Trim;
import static org.parboiled.Parboiled.createParser;

public class JtwigContentParser extends JtwigBaseParser<Compilable> {
    public static JtwigContentParser newParser(
            JtwigResource resource,
            ParserConfiguration configuration
    ) {
        return createParser(JtwigContentParser.class, resource, configuration);
    }

    public static Compilable parse(JtwigContentParser parser, JtwigResource input) throws ParseException {
        try {
            ReportingParseRunner<Compilable> runner = new ReportingParseRunner<>(parser.start());
            ParsingResult<Compilable> result = runner.run(
                    FileUtils.readAllText(input.retrieve(), Charset.defaultCharset()));
            return result.resultValue;
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

    Addon[] contentAddonParsers;
    Collection<Class<? extends BaseParser>> contentAddons;
    ParserConfiguration configuration;

    public JtwigContentParser(JtwigResource resource, ParserConfiguration configuration) {
        super(resource);
        basicParser = createParser(JtwigBasicParser.class, configuration);
        tagPropertyParser = createParser(JtwigTagPropertyParser.class, configuration);
        expressionParser = createParser(JtwigExpressionParser.class, resource, configuration);

        this.contentAddons = Collections2.transform(configuration.addons().list(), toBaseParser());
        this.configuration = configuration;

        contentAddonParsers = new Addon[contentAddons.size()];

        int i = 0;
        for (Class<? extends BaseParser> contentAddon : contentAddons) {
            contentAddonParsers[i++] = (Addon) createParser(contentAddon, resource, configuration);
        }
    }

    private Function<Class<? extends Addon>, Class<? extends BaseParser>> toBaseParser() {
        return new Function<Class<? extends Addon>, Class<? extends BaseParser>>() {
            @Nullable
            @Override
            public Class<? extends BaseParser> apply(@Nullable Class<? extends Addon> input) {
                return input;
            }
        };
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
                                        expressionParser.expression(),
                                        push(new Extends(expressionParser.pop())),
                                        basicParser.spacing(),
                                        basicParser.closeCode(),
                                        
                                        ZeroOrMore(
                                                basicParser.spacing(),
                                                block(),
                                                action(peek(1, Extends.class).add(pop(Block.class)))
                                        ),
                                        basicParser.spacing(),
                                        EOI
                                ),
                                new ParseException("Wrong extends syntax")
                        )
                )
        );
    }

    Rule normalTemplate() {
        return Sequence(
                content(),
                EOI
        );
    }

    Rule content() {
        return Sequence(
                push(new Sequence()),
                ZeroOrMore(
                        FirstOf(
                                addToContent(output()),
                                addToContent(block()),
                                addToContent(include()),
                                addToContent(embed()),
                                addToContent(macro()),
                                addToContent(importTemplate()),
                                addToContent(fromImportTemplate()),
//                                addToContent(filter()),
                                addToContent(forEach()),
                                addToContent(ifCondition()),
                                addToContent(set()),
                                addToContent(verbatim()),
                                addToContent(comment()),
                                addToContent(contentParsers()),
                                Sequence(
                                        openCode(),
                                        TestNot(
                                                FirstOf(
                                                        keyword(ENDBLOCK),
                                                        keyword(ENDFOR),
                                                        keyword(ENDIF),
                                                        keyword(ENDMACRO),
                                                        keyword(IF),
                                                        keyword(BLOCK),
                                                        keyword(FOR),
                                                        keyword(SET),
                                                        keyword(ELSEIF),
                                                        keyword(ELSE),
                                                        keyword(VERBATIM),
                                                        keyword(ENDFILTER),
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

    Rule contentAddon(Addon parser) {
        return Sequence(
                openCode(),
                basicParser.terminal(parser.beginKeyword()),
                basicParser.spacing(),
                parser.startRule(),
                mandatory(
                        Test(instanceOf(AddonModel.class).matches(peek())),
                        new ParseException(
                                "Addon parser not pushing a JtwigContentAddon object to the top of the stack")
                ),
                mandatory(
                        Sequence(
                                action(beforeBeginTrim()),
                                closeCode(),
                                action(afterBeginTrim()),
                                content(),
                                action(peek(1, AddonModel.class).withContent(pop(Sequence.class))),
                                openCode(),
                                basicParser.terminal(parser.endKeyword()),
                                basicParser.spacing(),
                                action(beforeEndTrim()),
                                closeCode(),
                                action(afterEndTrim())
                        ),
                        new ParseException("Wrong syntax for " + parser.beginKeyword())
                )
        );
    }

    Rule addToContent(Rule innerRule) {
        return Sequence(
                innerRule,
                action(peek(1, Sequence.class).add(pop()))
        );
    }

    Rule block() {
        return Sequence(
                openCode(),
                keyword(BLOCK),
                mandatory(
                        Sequence(
                                expressionParser.identifierAsString(),
                                push(new Block(expressionParser.popIdentifierAsString())),
                                action(beforeBeginTrim()),
                                closeCode(),
                                action(afterBeginTrim()),
                                content(),
                                action(peek(1, Block.class).withContent(pop(Sequence.class))),
                                openCode(),
                                action(beforeEndTrim()),
                                keyword(ENDBLOCK),
                                Optional(
                                        expressionParser.variable(),
                                        assertEqual(
                                                peek(Block.class).name(),
                                                (String) expressionParser.pop(Constant.class).getValue()
                                        )
                                ),
                                closeCode(),
                                action(afterEndTrim())
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
                                action(beforeBeginTrim()),
                                Optional(
                                        keyword(JtwigKeyword.IGNORE_MISSING),
                                        action(peek(Include.class).setIgnoreMissing(true))
                                ),
                                Optional(
                                        keyword(JtwigKeyword.WITH),
                                        FirstOf(expressionParser.map(), expressionParser.variable()),
                                        action(peek(1, Include.class).with(expressionParser.pop()))
                                ),
                                Optional(
                                        keyword(JtwigKeyword.ONLY),
                                        action(peek(Include.class).setIsolated(true))
                                ),
                                closeCode(),
                                action(afterEndTrim())
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
                                push(new Extends(basicParser.pop())),
                                ZeroOrMore(
                                        basicParser.spacing(),
                                        block(),
                                        action(peek(1, Extends.class).add(pop(Block.class)))
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
    
    Rule macro() {
        return Sequence(
                openCode(),
                keyword(MACRO),
                mandatory(
                        Sequence(
                                expressionParser.identifierAsString(),
                                push(new Macro(currentPosition(), expressionParser.popIdentifierAsString())),
                                symbolWithSpacing(OPEN_PARENT),
                                Optional(
                                        expressionParser.variable(),
                                        action(peek(1, Macro.class).add(expressionParser.pop(Variable.class).name())),
                                        ZeroOrMore(
                                                symbolWithSpacing(COMMA),
                                                expressionParser.variable(),
                                                action((peek(1, Macro.class)).add(expressionParser.pop(Variable.class).name()))
                                        )
                                ),
                                symbolWithSpacing(CLOSE_PARENT),
                                action(beforeBeginTrim()),
                                closeCode(),
                                action(afterBeginTrim()),
                                content(),
                                action(peek(1, Macro.class).withContent(pop(Sequence.class))),
                                openCode(),
                                action(beforeEndTrim()),
                                keyword(ENDMACRO),
                                Optional(
                                        expressionParser.variable(),
                                        assertEqual(
                                                peek(1, Macro.class).name(),
                                                (String) expressionParser.pop(Variable.class).name()
                                        )
                                ),
                                closeCode(),
                                action(afterEndTrim())
                        ),
                        new ParseException("Wrong macro syntax")
                )
        );
    }
    
    Rule importTemplate() {
        return Sequence(
                openCode(),
                keyword(IMPORT),
                mandatory(
                        Sequence(
                                importLocation(),
                                keyword(AS),
                                expressionParser.variable(),
                                push(new Import.General(currentPosition(), expressionParser.pop(1), expressionParser.pop(Variable.class).name())),
                                closeCode()
                        ),
                        new ParseException("Inavlid import syntax")
                )
        );
    }
    Rule fromImportTemplate() {
        return Sequence(
                openCode(),
                keyword(FROM),
                importLocation(),
                push(new Import.From(currentPosition(), expressionParser.pop())),
                keyword(IMPORT),
                mandatory(
                        Sequence(
                                importDefinition(),
                                ZeroOrMore(
                                        symbolWithSpacing(COMMA),
                                        importDefinition()
                                ),
                                closeCode()
                        ),
                        new ParseException("Inavlid import syntax")
                )
        );
    }
    Rule importLocation() {
        return FirstOf(
                Sequence(
                        keyword(SELF),
                        expressionParser.push(new Import.SelfReference(currentPosition()))
                ),
                // Constants are checked for separately to ease some parse-time
                // checking in the Import class
                expressionParser.constant(),
                expressionParser.expression()
        );
    }
    Rule importDefinition() {
        return Sequence(
                expressionParser.variable(),
                FirstOf(
                        Sequence(
                                keyword(AS),
                                expressionParser.variable(),
                                action(peek(2, Import.From.class).add(expressionParser.pop(1, Variable.class).name(), expressionParser.pop(Variable.class).name()))
                        ),
                        action(peek(1, Import.From.class).add(expressionParser.pop(Variable.class).name(), null))
                )
        );
    }

    Rule text() {
        return Sequence(
                push(new Text.Builder()),
                OneOrMore(
                        FirstOf(
                                Sequence(
                                        basicParser.escape(),
                                        action(peek(Text.Builder.class).append(match()))
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
                                        action(peek(Text.Builder.class).append(match()))
                                )
                        )
                ).suppressSubnodes(),
                push(pop(Text.Builder.class).build())
        );
    }

    Rule verbatim() {
        return Sequence(
                openCode(),
                keyword(VERBATIM),
                mandatory(
                        Sequence(
                                push(new Verbatim()),
                                action(beforeBeginTrim()),
                                closeCode(),
                                text(Sequence(
                                        basicParser.openCode(),
                                        basicParser.spacing(),
                                        keyword(ENDVERBATIM)
                                )),
                                action(peek(1, Verbatim.class).withContent(new Sequence().add(pop(Compilable.class)))),
                                openCode(),
                                keyword(JtwigKeyword.ENDVERBATIM),
                                closeCode(),
                                action(afterEndTrim())
                        ),
                        new ParseException("Wrong verbatim syntax")
                )
        );
    }

    Rule text(Rule until) {
        return Sequence(
                push(new Text.Builder()),
                OneOrMore(
                        FirstOf(
                                Sequence(
                                        basicParser.escape(),
                                        action(peek(Text.Builder.class).append(match()))
                                ),
                                Sequence(
                                        TestNot(
                                                until
                                        ),
                                        ANY,
                                        action(peek(Text.Builder.class).append(match()))
                                )
                        )
                ).suppressSubnodes(),
                push(pop(Text.Builder.class).build())
        );
    }

    Rule ifCondition() {
        return Sequence(
                openCode(),
                keyword(IF),
                mandatory(
                        Sequence(
                                push(new IfControl()),
                                expressionParser.expression(),
                                push(new IfControl.Case(expressionParser.pop())),
                                action(beforeBeginTrim()),
                                closeCode(),
                                action(afterBeginTrim()),
                                content(),
                                action(peek(1, IfControl.Case.class).withContent(pop(Sequence.class))),
                                ZeroOrMore(
                                        Sequence(
                                                action(peek(1, IfControl.class).add(peek(IfControl.Case.class))),
                                                openCode(),
                                                keyword(ELSEIF),
                                                expressionParser.expression(),
                                                push(new IfControl.Case(expressionParser.pop())),
                                                action(beforeEndTrim(1)),
                                                action(beforeBeginTrim()),
                                                closeCode(),
                                                action(afterEndTrim(1)),
                                                action(afterBeginTrim()),
                                                action(pop(1)),
                                                content(),
                                                action(peek(1, IfControl.Case.class).withContent(pop(Sequence.class)))
                                        )
                                ),
                                Optional(
                                        Sequence(
                                                action(peek(1, IfControl.class).add(peek(IfControl.Case.class))),
                                                openCode(),
                                                keyword(ELSE),
                                                push(new IfControl.Case(new Constant<>(true))),
                                                action(beforeEndTrim(1)),
                                                action(beforeBeginTrim()),
                                                closeCode(),
                                                action(afterEndTrim(1)),
                                                action(afterBeginTrim()),
                                                action(pop(1)),
                                                content(),
                                                action(peek(1, IfControl.Case.class).withContent(pop(Sequence.class)))
                                        )
                                ),
                                action(peek(1, IfControl.class).add(peek(IfControl.Case.class))),
                                openCode(),
                                action(beforeEndTrim()),
                                keyword(ENDIF),
                                closeCode(),
                                action(afterEndTrim()),
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
                                expressionParser.identifierAsString(),
                                FirstOf(
                                        Sequence(
                                                symbolWithSpacing(COMMA),
                                                expressionParser.identifierAsString(),
                                                keyword(IN),
                                                expressionParser.expression(),
                                                push(new For(expressionParser.popIdentifierAsString(2),
                                                        expressionParser.popIdentifierAsString(1),
                                                        expressionParser.pop()))
                                        ),
                                        Sequence(
                                                keyword(IN),
                                                expressionParser.expression(),
                                                push(new For(expressionParser.popIdentifierAsString(1),
                                                        expressionParser.pop()))
                                        )
                                ),
                                action(beforeBeginTrim()),
                                closeCode(),
                                action(afterBeginTrim()),
                                content(),
                                action(peek(1, Content.class).withContent(pop(Sequence.class))),
                                Optional(
                                        Sequence(
                                                openCode(),
                                                action(beforeEndTrim()),
                                                keyword(ELSE),
                                                closeCode(),
                                                action(afterBeginTrim()),
                                                content(),
                                                action(peek(1, For.class).withElse(pop(Sequence.class)))
                                        )
                                ),
                                
                                openCode(),
                                action(beforeEndTrim()),
                                keyword(ENDFOR),
                                closeCode(),
                                action(afterEndTrim())
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
                                expressionParser.identifierAsString(),
                                symbolWithSpacing(ATTR),
                                expressionParser.expression(),
                                push(new SetVariable(expressionParser.popIdentifierAsString(1), expressionParser.pop())),
                                action(beforeBeginTrim()),
                                closeCode(),
                                action(afterEndTrim())
                        ),
                        new ParseException("Wrong set syntax")
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
                                push(new Output(expressionParser.pop())),
                                action(beforeBeginTrim()),
                                tagPropertyParser.property(),
                                action(afterEndTrim()),
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

    Rule comment() {
        return Sequence(
                push(new Comment()),
                basicParser.openComment(),
                tagPropertyParser.property(),
                action(beforeBeginTrim()),
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
                action(afterEndTrim()),
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


    WhiteSpaceControl afterEndTrim(int p) {
        return peek(p, Tag.class).tag().whiteSpaceControl().trimAfterEnd(tagPropertyParser.getCurrentProperty() == Trim);
    }

    WhiteSpaceControl beforeEndTrim(int p) {
        return peek(p, Tag.class).tag().whiteSpaceControl().trimBeforeEnd(tagPropertyParser.getCurrentProperty() == Trim);
    }

    WhiteSpaceControl afterBeginTrim(int p) {
        return peek(p, Tag.class).tag().whiteSpaceControl().trimAfterBegin(tagPropertyParser.getCurrentProperty() == Trim);
    }

    WhiteSpaceControl beforeBeginTrim(int p) {
        return peek(p, Tag.class).tag().whiteSpaceControl().trimBeforeBegin(tagPropertyParser.getCurrentProperty() == Trim);
    }

    WhiteSpaceControl afterEndTrim() {
        return afterEndTrim(0);
    }

    WhiteSpaceControl beforeEndTrim() {
        return beforeEndTrim(0);
    }

    WhiteSpaceControl afterBeginTrim() {
        return afterBeginTrim(0);
    }

    WhiteSpaceControl beforeBeginTrim() {
        return beforeBeginTrim(0);
    }


}
