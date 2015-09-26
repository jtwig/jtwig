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

import org.jtwig.extension.core.tokenparsers.model.SetVariable;
import org.jtwig.exception.ParseException;
import org.jtwig.extension.api.tokenparser.TokenParser;
import org.jtwig.loader.Loader;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.parboiled.Rule;

public class SetStatementParser extends TokenParser {

    public SetStatementParser(Loader.Resource resource, JtwigContentParser content, JtwigBasicParser basic, JtwigExpressionParser expr, JtwigTagPropertyParser tag) {
        super(resource, content, basic, expr, tag);
    }

    @Override
    public Rule rule() {
        return Sequence(
                content.openCode(),
                basic.spacing(),
                basic.keyword("set"),
                basic.spacing(),
                mandatory(
                        Sequence(
                                expr.identifierAsString(),
                                basic.spacing(),
                                "=",
                                basic.spacing(),
                                expr.expression(),
                                push(new SetVariable(expr.popIdentifierAsString(1), expr.pop())),
                                basic.spacing(),
                                action(content.beforeBeginTrim()),
                                content.closeCode(),
                                action(content.afterEndTrim())
                        ),
                        new ParseException("Wrong set syntax")
                )
        );
    }

    @Override
    public java.lang.String[] keywords() {
        return new String[]{"set"};
    }
    
}