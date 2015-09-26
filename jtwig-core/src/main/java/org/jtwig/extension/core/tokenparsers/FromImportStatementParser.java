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

import org.jtwig.extension.core.tokenparsers.model.Import;
import org.jtwig.exception.ParseException;
import org.jtwig.loader.Loader;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.parboiled.Rule;

public class FromImportStatementParser extends ImportStatementParser {

    public FromImportStatementParser(Loader.Resource resource, JtwigContentParser content, JtwigBasicParser basic, JtwigExpressionParser expr, JtwigTagPropertyParser tag) {
        super(resource, content, basic, expr, tag);
    }

    @Override
    public Rule rule() {
        return Sequence(
                content.openCode(),
                basic.keyword("from"),
                basic.spacing(),
                importLocation(),
                push(new Import.From(currentPosition(), expr.pop())),
                basic.spacing(),
                basic.keyword("import"),
                basic.spacing(),
                mandatory(
                        Sequence(
                                importDefinition(),
                                basic.spacing(),
                                ZeroOrMore(
                                        ",",
                                        basic.spacing(),
                                        importDefinition(),
                                        basic.spacing()
                                ),
                                content.closeCode()
                        ),
                        new ParseException("Inavlid import syntax")
                )
        );
    }
    public Rule importDefinition() {
        return Sequence(
                expr.identifierAsString(),
                basic.spacing(),
                FirstOf(
                        Sequence(
                                basic.keyword("as"),
                                basic.spacing(),
                                expr.identifierAsString(),
                                basic.spacing(),
                                action(peek(2, Import.From.class).add(expr.popIdentifierAsString(1), expr.popIdentifierAsString()))
                        ),
                        action(peek(1, Import.From.class).add(expr.popIdentifierAsString(), null))
                )
        );
    }
    
}