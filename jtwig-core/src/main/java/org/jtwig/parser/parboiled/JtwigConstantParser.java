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

import org.jtwig.expressions.model.Constant;
import org.jtwig.parser.config.ParserConfiguration;
import org.parboiled.BaseParser;
import org.parboiled.Rule;

import static org.jtwig.parser.model.JtwigKeyword.*;
import static org.jtwig.parser.model.JtwigSymbol.DOT;
import static org.parboiled.Parboiled.createParser;

public class JtwigConstantParser extends BaseParser<Constant> {
    final JtwigBasicParser basic;

    public JtwigConstantParser(ParserConfiguration parserConfiguration) {
        this.basic  = createParser(JtwigBasicParser.class, parserConfiguration);
    }

    public JtwigConstantParser() {
        this(new ParserConfiguration());
    }

    public Rule anyConstant () {
        return FirstOf(
                nullValue(),
                booleanValue(),
                doubleValue(),
                integerValue(),
                charValue(),
                string()
        );
    }

    public Rule booleanValue() {
        return FirstOf(
                Sequence(
                        basic.keyword(TRUE),
                        push(new Constant<>(true))
                ),
                Sequence(
                        basic.keyword(FALSE),
                        push(new Constant<>(false))
                )
        );
    }

    public Rule nullValue() {
        return Sequence(
                basic.keyword(NULL),
                push(new Constant<>(null))
        );
    }

    public Rule charValue () {
        return Sequence(
                basic.onlyOneChar(),
                push(new Constant<>(basic.pop().charAt(0)))
        );
    }

    public Rule integerValue() {
        return Sequence(
                OneOrMore(basic.digit()),
                push(new Constant<>(Integer.parseInt(match())))
        );
    }

    public Rule doubleValue() {
        return Sequence(
                Sequence(
                        OneOrMore(basic.digit()),
                        basic.symbol(DOT),
                        OneOrMore(basic.digit())
                ),
                push(new Constant<>(Double.valueOf(match())))
        );
    }

    public Rule string() {
        return Sequence(
                basic.stringLiteral(),
                push(new Constant<>(basic.pop()))
        );
    }
}
