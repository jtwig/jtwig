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

package com.lyncode.jtwig.parser.parboiled;

import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.parser.model.JtwigSymbol;
import com.lyncode.jtwig.parser.model.JtwigTagProperty;
import org.parboiled.BaseParser;
import org.parboiled.Rule;

import static org.parboiled.Parboiled.createParser;

public class JtwigTagPropertyParser extends BaseParser<JtwigTagProperty> {
    JtwigTagProperty property = JtwigTagProperty.None;
    final JtwigBasicParser basicParser;

    public JtwigTagPropertyParser(ParserConfiguration configuration) {
        basicParser = createParser(JtwigBasicParser.class, configuration);
    }

    Rule property() {
        return FirstOf(
                Sequence(
                        basicParser.symbol(JtwigSymbol.MINUS),
                        setProperty(JtwigTagProperty.Trim)
                ),
                setProperty(JtwigTagProperty.None)
        );
    }

    boolean setProperty(JtwigTagProperty property) {
        this.property = property;
        return true;
    }

    public JtwigTagProperty getCurrentProperty () {
        return property;
    }
}
