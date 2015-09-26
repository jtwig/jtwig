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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import javax.annotation.Nullable;
import static org.hamcrest.CoreMatchers.instanceOf;
import org.jtwig.Environment;
import org.jtwig.addons.Addon;
import org.jtwig.addons.AddonModel;
import org.jtwig.content.api.Compilable;
import org.jtwig.content.api.Tag;
import org.jtwig.content.api.ability.ElementList;
import org.jtwig.content.api.ability.ElementTracker;
import org.jtwig.content.model.Template;
import org.jtwig.content.model.compilable.Output;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.content.model.compilable.Text;
import org.jtwig.content.model.tag.WhiteSpaceControl;
import org.jtwig.exception.ParseException;
import org.jtwig.extension.api.tokenparser.TokenParser;
import org.jtwig.loader.Loader;
import org.jtwig.parser.model.JtwigKeyword;
import org.jtwig.parser.model.JtwigSymbol;
import static org.jtwig.parser.model.JtwigTagProperty.Trim;
import org.parboiled.BaseParser;
import static org.parboiled.Parboiled.createParser;
import org.parboiled.Rule;

public class JtwigContentParser extends JtwigBaseParser<Compilable> {

    final JtwigBasicParser basicParser;
    final JtwigExpressionParser expressionParser;
    final JtwigTagPropertyParser tagPropertyParser;

    Addon[] contentAddonParsers;
    Collection<Class<? extends BaseParser>> contentAddons;
    Environment env;
    Collection<TokenParser> tokenParsers = new ArrayList<>();

    public JtwigContentParser(Loader.Resource resource, Environment env) {
        super(resource);
        basicParser = env.getBasicParser();
        tagPropertyParser = env.getTagPropertyParser();
        expressionParser = createParser(JtwigExpressionParser.class, resource, env);

        this.contentAddons = Collections2.transform(env.getConfiguration().getExtensions().getAddons(), toBaseParser());
        this.env = env;

        contentAddonParsers = new Addon[contentAddons.size()];

        int i = 0;
        for (Class<? extends BaseParser> contentAddon : contentAddons) {
            contentAddonParsers[i++] = (Addon) createParser(contentAddon, resource, env);
        }
        
        for (Class<? extends TokenParser> tokenParser : env.getConfiguration().getExtensions().getTokenParsers()) {
            tokenParsers.add(createParser(tokenParser, resource, this, basicParser, expressionParser, tagPropertyParser));
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
        return Sequence(
                push(new Template(currentPosition())),
                content(),
                action(peek(1, Template.class).add(pop(Sequence.class))),
                EOI
        );
    }
    
    public Rule content() {
        return Sequence(
                push(new Sequence()),
                ZeroOrMore(
                        FirstOf(
                                addToContent(output()),
//                                addToContent(comment()),
                                tokenParsers(),
                                addToContent(contentParsers()),
                                Sequence(
                                        openCode(),
                                        TestNot(
                                                tokenParserKeywords()
                                        ),
                                        TestNot(
                                                addonTerminators()
                                        ),
                                        expressionParser.identifierAsString(),
                                        throwException(new ParseException("Unknown tag: "+expressionParser.popIdentifierAsString()))
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
    
    Rule tokenParsers() {
        if (tokenParsers.isEmpty()) {
            return Test(false);
        }
        Rule[] rules = new Rule[tokenParsers.size()];
        int i = -1;
        for (TokenParser tp : tokenParsers) {
            Rule tmp = tp.rule();
            if (tp.addToTracker() && !tp.addToContent()) {
                tmp = addToElementTracker(tmp, true);
            } else if (tp.addToTracker()) {
                tmp = addToElementTracker(tmp);
            }
            if (tp.addToContent()) {
                tmp = addToContent(tmp);
            }
            rules[++i] = tmp;
        }
        return FirstOf(rules);
    }
    
    Rule tokenParserKeywords() {
        if (tokenParsers.isEmpty()) {
            return Test(false);
        }
        Collection<String> keywords = new HashSet<>();
        for (TokenParser tp : tokenParsers) {
            keywords.addAll(Arrays.asList(tp.keywords()));
        }
        // Sort by length, large first
        keywords = new ArrayList<>(keywords);
        Collections.sort((List<String>)keywords, new Comparator<String>() {
            @Override
            public int compare(String t, String t1) {
                return Integer.valueOf(t1.length()).compareTo(Integer.valueOf(t.length()));
            }
        });
        return FirstOf(keywords.toArray(new String[keywords.size()]));
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
    Rule addonTerminators() {
        if (contentAddonParsers.length == 0) {
            return Test(false);
        }
        Rule[] rules = new Rule[contentAddonParsers.length];
        for (int i = 0; i < contentAddonParsers.length; i++) {
            rules[i] = String(contentAddonParsers[i].endKeyword());
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
                                Optional(content()),
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

    public Rule addToContent(Rule innerRule) {
        return Sequence(
                innerRule,
                action(peek(1, Sequence.class).add(pop()))
        );
    }
    public Rule addToElementTracker(Rule innerRule) {
        return addToElementTracker(innerRule, false);
    }
    public Rule addToElementTracker(Rule innerRule, boolean pop) {
        return Sequence(
                innerRule,
                action(lastElementTracker().track(pop ? pop() : peek()))
        );
    }
    Rule addToElementList(Rule innerRule) {
        return addToElementList(innerRule, false);
    }
    Rule addToElementList(Rule innerRule, boolean pop) {
        return Sequence(
                innerRule,
                action(lastElementList().add(pop ? pop() : peek()))
        );
    }

    public Rule text() {
        return text(FirstOf(
                basicParser.openCode(),
                basicParser.openOutput(),
                basicParser.openComment()
        ));
    }

    public Rule text(Rule until) {
        return Sequence(
                push(new Text()),
                OneOrMore(
                        FirstOf(
                                Sequence(
                                        basicParser.escape(),
                                        action(peek(Text.class).append(match()))
                                ),
                                Sequence(
                                        TestNot(
                                                until
                                        ),
                                        ANY,
                                        action(peek(Text.class).append(match()))
                                )
                        )
                ).suppressSubnodes()
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

    public Rule openCode() {
        return Sequence(
                basicParser.openCode(),
                tagPropertyParser.property(),
                basicParser.spacing()
        );
    }

    public Rule closeCode() {
        return Sequence(
                basicParser.spacing(),
                tagPropertyParser.property(),
                basicParser.closeCode(),
                Optional(FirstOf("\r\n", "\n"))
        );
    }

    Rule keyword(JtwigKeyword keyword) {
        return Sequence(
                basicParser.keyword(keyword),
                basicParser.spacing()
        );
    }
    Rule keyword(String keyword) {
        return Sequence(
                basicParser.keyword(keyword),
                basicParser.spacing()
        );
    }


    public WhiteSpaceControl afterEndTrim(int p) {
        return peek(p, Tag.class).tag().whiteSpaceControl().trimAfterClose(tagPropertyParser.getCurrentProperty() == Trim);
    }

    public WhiteSpaceControl beforeEndTrim(int p) {
        return peek(p, Tag.class).tag().whiteSpaceControl().trimBeforeClose(tagPropertyParser.getCurrentProperty() == Trim);
    }

    public WhiteSpaceControl afterBeginTrim(int p) {
        return peek(p, Tag.class).tag().whiteSpaceControl().trimAfterOpen(tagPropertyParser.getCurrentProperty() == Trim);
    }

    public WhiteSpaceControl beforeBeginTrim(int p) {
        return peek(p, Tag.class).tag().whiteSpaceControl().trimBeforeOpen(tagPropertyParser.getCurrentProperty() == Trim);
    }

    public WhiteSpaceControl afterEndTrim() {
        return afterEndTrim(0);
    }

    public WhiteSpaceControl beforeEndTrim() {
        return beforeEndTrim(0);
    }

    public WhiteSpaceControl afterBeginTrim() {
        return afterBeginTrim(0);
    }

    public WhiteSpaceControl beforeBeginTrim() {
        return beforeBeginTrim(0);
    }

    //~ Helpers ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    ElementList lastElementList() {
        return last(ElementList.class);
    }
    ElementTracker lastElementTracker() {
        return last(ElementTracker.class);
    }
    Template lastTemplate() {
        return last(Template.class);
    }
}
