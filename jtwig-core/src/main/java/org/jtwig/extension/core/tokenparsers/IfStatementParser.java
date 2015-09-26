/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.extension.core.tokenparsers;

import org.jtwig.extension.core.tokenparsers.model.IfControl;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.exception.ParseException;
import org.jtwig.expressions.model.Constant;
import org.jtwig.extension.api.tokenparser.TokenParser;
import org.jtwig.loader.Loader;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.parboiled.Rule;

public class IfStatementParser extends TokenParser {

    public IfStatementParser(Loader.Resource resource, JtwigContentParser content, JtwigBasicParser basic, JtwigExpressionParser expr, JtwigTagPropertyParser tag) {
        super(resource, content, basic, expr, tag);
    }
    
    @Override
    public Rule rule() {
        return Sequence(
                content.openCode(),
                push(new IfControl()),
                basic.keyword("if"),
                basic.spacing(),
                mandatory(
                        Sequence(
                                getExpressionParser().expression(),
                                basic.spacing(),
                                push(new IfControl.Case(getExpressionParser().pop())),
                                action(content.beforeBeginTrim()),
                                action(content.beforeBeginTrim(1)),
                                content.closeCode(),
                                action(content.afterBeginTrim()),
                                action(content.afterBeginTrim(1)),
                                content.content(),
                                action(peek(1, IfControl.Case.class).withContent(pop(Sequence.class))),
                                ZeroOrMore(
                                        Sequence(
                                                action(peek(1, IfControl.class).add(peek(IfControl.Case.class))),
                                                content.openCode(),
                                                basic.keyword("elseif"),
                                                basic.spacing(),
                                                getExpressionParser().expression(),
                                                push(new IfControl.Case(getExpressionParser().pop())),
                                                action(content.beforeEndTrim(1)),
                                                action(content.beforeBeginTrim()),
                                                basic.spacing(),
                                                content.closeCode(),
                                                action(content.afterBeginTrim()),
                                                action(pop(1)),
                                                content.content(),
                                                action(peek(1, IfControl.Case.class).withContent(pop(Sequence.class)))
                                        )
                                ),
                                Optional(
                                        Sequence(
                                                action(peek(1, IfControl.class).add(peek(IfControl.Case.class))),
                                                content.openCode(),
                                                basic.spacing(),
                                                basic.keyword("else"),
                                                basic.spacing(),
                                                push(new IfControl.Case(new Constant<>(true))),
                                                action(content.beforeEndTrim(1)),
                                                action(content.beforeBeginTrim()),
                                                content.closeCode(),
                                                action(content.afterBeginTrim()),
                                                action(pop(1)),
                                                content.content(),
                                                action(peek(1, IfControl.Case.class).withContent(pop(Sequence.class)))
                                        )
                                ),
                                action(peek(1, IfControl.class).add(peek(IfControl.Case.class))),
                                content.openCode(),
                                basic.keyword("endif"),
                                basic.spacing(),
                                action(content.beforeEndTrim(1)),
                                action(content.beforeEndTrim()),
                                content.closeCode(),
                                action(content.afterEndTrim(1)),
                                action(content.afterEndTrim()),
                                action(pop())
                        ),
                        new ParseException("Wrong if syntax")
                )
        );
    }

    @Override
    public java.lang.String[] keywords() {
        return new String[]{"if","elseif","else","endif"};
    }
    
}