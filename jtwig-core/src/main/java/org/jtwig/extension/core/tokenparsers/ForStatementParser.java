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

import org.jtwig.content.model.compilable.Content;
import org.jtwig.extension.core.tokenparsers.model.For;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.exception.ParseException;
import org.jtwig.extension.api.tokenparser.TokenParser;
import org.jtwig.loader.Loader;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.parboiled.Rule;

public class ForStatementParser extends TokenParser {

    public ForStatementParser(Loader.Resource resource, JtwigContentParser content, JtwigBasicParser basic, JtwigExpressionParser expr, JtwigTagPropertyParser tag) {
        super(resource, content, basic, expr, tag);
    }

    @Override
    public Rule rule() {
        return Sequence(
                content.openCode(),
                basic.spacing(),
                basic.keyword("for"),
                basic.spacing(),
                mandatory(
                        Sequence(
                                basic.spacing(),
                                expr.identifierAsString(),
                                basic.spacing(),
                                FirstOf(
                                        Sequence(
                                                basic.spacing(),
                                                ",",
                                                basic.spacing(),
                                                expr.identifierAsString(),
                                                basic.spacing(),
                                                basic.keyword("in"),
                                                basic.spacing(),
                                                expr.expression(),
                                                push(new For(expr.popIdentifierAsString(2),
                                                        expr.popIdentifierAsString(1),
                                                        expr.pop()))
                                        ),
                                        Sequence(
                                                basic.spacing(),
                                                basic.keyword("in"),
                                                basic.spacing(),
                                                expr.expression(),
                                                basic.spacing(),
                                                push(new For(expr.popIdentifierAsString(1),
                                                        expr.pop()))
                                        )
                                ),
//                                action(content.beforeBeginTrim()),
                                basic.spacing(),
                                content.closeCode(),
//                                action(afterBeginTrim()),
                                content.content(),
                                action(peek(1, Content.class).withContent(pop(Sequence.class))),
                                Optional(
                                        Sequence(
                                                content.openCode(),
//                                                action(beforeEndTrim()),
                                                basic.spacing(),
                                                basic.keyword("else"),
                                                basic.spacing(),
                                                content.closeCode(),
//                                                action(afterBeginTrim()),
                                                content.content(),
                                                action(peek(1, For.class).withElse(pop(Sequence.class)))
                                        )
                                ),
                                content.openCode(),
//                                action(beforeEndTrim()),
                                basic.spacing(),
                                basic.keyword("endfor"),
                                basic.spacing(),
                                content.closeCode()
//                                action(afterEndTrim())
                        ),
                        new ParseException("Wrong for each syntax")
                )
        );
    }

    @Override
    public String[] keywords() {
        return new String[]{"for", "endfor", "else"};
    }
    
}
