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

import org.jtwig.extension.core.tokenparsers.model.Macro;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.exception.ParseException;
import org.jtwig.expressions.model.Variable;
import org.jtwig.extension.api.tokenparser.TokenParser;
import org.jtwig.loader.Loader;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.parboiled.Rule;

public class MacroDefinitionParser extends TokenParser {

    public MacroDefinitionParser(Loader.Resource resource, JtwigContentParser content, JtwigBasicParser basic, JtwigExpressionParser expr, JtwigTagPropertyParser tag) {
        super(resource, content, basic, expr, tag);
    }

    @Override
    public Rule rule() {
        return Sequence(
                content.openCode(),
                basic.spacing(),
                basic.keyword("macro"),
                basic.spacing(),
                mandatory(
                        Sequence(
                                expr.identifierAsString(),
                                push(new Macro(currentPosition(), expr.popIdentifierAsString())),
                                basic.spacing(),
                                "(",
                                basic.spacing(),
                                Optional(
                                        expr.variable(),
                                        action(peek(1, Macro.class).add(expr.pop(Variable.class).name())),
                                        basic.spacing(),
                                        ZeroOrMore(
                                                ",",
                                                basic.spacing(),
                                                expr.variable(),
                                                action((peek(1, Macro.class)).add(expr.pop(Variable.class).name())),
                                                basic.spacing()
                                        )
                                ),
                                ")",
                                basic.spacing(),
//                                action(beforeBeginTrim()),
                                content.closeCode(),
//                                action(afterBeginTrim()),
                                content.content(),
                                action(peek(1, Macro.class).withContent(pop(Sequence.class))),
                                content.openCode(),
//                                action(beforeEndTrim()),
                                basic.spacing(),
                                basic.keyword("endmacro"),
                                basic.spacing(),
                                Optional(
                                        expr.identifierAsString(),
                                        assertEqual(
                                                peek(1, Macro.class).name(),
                                                expr.popIdentifierAsString()
                                        ),
                                        basic.spacing()
                                ),
                                content.closeCode()
//                                action(afterEndTrim())
                        ),
                        new ParseException("Wrong macro syntax")
                )
        );
    }

    @Override
    public java.lang.String[] keywords() {
        return new String[]{"macro","endmacro"};
    }

    @Override
    public boolean addToContent() {
        return false;
    }

    @Override
    public boolean addToTracker() {
        return true;
    }

    public boolean assertEqual(String value1, String value2) {
        if (!value1.equals(value2)) {
            return throwException(new ParseException("Start statement and ending block names do not match"));
        } else {
            return true;
        }
    }
    
}
