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
import org.jtwig.expressions.model.Variable;
import org.jtwig.extension.api.tokenparser.TokenParser;
import org.jtwig.loader.Loader;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.parboiled.Rule;

public class ImportStatementParser extends TokenParser {

    public ImportStatementParser(Loader.Resource resource, JtwigContentParser content, JtwigBasicParser basic, JtwigExpressionParser expr, JtwigTagPropertyParser tag) {
        super(resource, content, basic, expr, tag);
    }

    @Override
    public Rule rule() {
        return Sequence(
                content.openCode(),
                basic.keyword("import"),
                basic.spacing(),
                mandatory(
                        Sequence(
                                importLocation(),
                                basic.spacing(),
                                basic.keyword("as"),
                                basic.spacing(),
                                expr.variable(),
                                push(new Import.General(currentPosition(), expr.pop(1), expr.pop(Variable.class).name())),
                                basic.spacing(),
                                content.closeCode()
                        ),
                        new ParseException("Inavlid import syntax")
                )
        );
    }

    @Override
    public java.lang.String[] keywords() {
        return new String[]{"import"};
    }
    
    //~ Ancillary rules ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Rule importLocation() {
        return FirstOf(
                Sequence(
                        basic.keyword("_self"),
                        expr.push(new Import.SelfReference(currentPosition()))
                ),
                // Constants are checked for separately to ease some parse-time
                // checking in the Import class
                expr.constant(),
                expr.expression()
        );
    }
    
}