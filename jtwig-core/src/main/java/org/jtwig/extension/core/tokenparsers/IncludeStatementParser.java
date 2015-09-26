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

import org.jtwig.extension.core.tokenparsers.model.Include;
import org.jtwig.exception.ParseException;
import org.jtwig.extension.api.tokenparser.TokenParser;
import org.jtwig.loader.Loader;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.parboiled.Rule;
import org.parboiled.annotations.Label;

public class IncludeStatementParser extends TokenParser {

    public IncludeStatementParser(Loader.Resource resource, JtwigContentParser content, JtwigBasicParser basic, JtwigExpressionParser expr, JtwigTagPropertyParser tag) {
        super(resource, content, basic, expr, tag);
    }

    @Label("Include rule")
    @Override
    public Rule rule() {
        return Sequence(
                content.openCode(),
                basic.spacing(),
                basic.keyword("include"),
                basic.spacing(),
                mandatory(
                        Sequence(
                                FirstOf(
                                        expr.constant(),
                                        expr.expression()
                                ),
                                basic.spacing(),
                                push(new Include(currentPosition(), expr.pop())),
//                                action(beforeBeginTrim()),
                                Optional(
                                        basic.keyword("ignore missing"),
                                        basic.spacing(),
                                        action(peek(Include.class).setIgnoreMissing(true))
                                ),
                                Optional(
                                        basic.keyword("with"),
                                        basic.spacing(),
                                        FirstOf(expr.map(), expr.variable()),
                                        basic.spacing(),
                                        action(peek(1, Include.class).with(expr.pop()))
                                ),
                                Optional(
                                        basic.keyword("only"),
                                        basic.spacing(),
                                        action(peek(Include.class).setIsolated(true))
                                ),
                                basic.spacing(),
                                content.closeCode()
//                                action(afterEndTrim())
                        ),
                        new ParseException("Wrong include syntax")
                )
        );
    }

    @Override
    public java.lang.String[] keywords() {
        return new String[]{"include"};
    }
    
}