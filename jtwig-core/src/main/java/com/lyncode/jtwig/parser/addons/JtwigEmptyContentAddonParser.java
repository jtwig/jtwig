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

package com.lyncode.jtwig.parser.addons;

import com.lyncode.jtwig.parser.JtwigBasicParser;
import com.lyncode.jtwig.parser.JtwigExpressionParser;
import com.lyncode.jtwig.parser.JtwigTagPropertyParser;
import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;

public abstract class JtwigEmptyContentAddonParser extends BaseParser<JtwigEmptyContentAddon> {
    final JtwigBasicParser basicParser;
    final JtwigExpressionParser expressionParser;
    final JtwigTagPropertyParser tagPropertyParser;

    public JtwigEmptyContentAddonParser () {
        basicParser = Parboiled.createParser(JtwigBasicParser.class);
        expressionParser = Parboiled.createParser(JtwigExpressionParser.class);
        tagPropertyParser = Parboiled.createParser(JtwigTagPropertyParser.class);
    }


    public JtwigBasicParser basicParser() {
        return basicParser;
    }

    public JtwigExpressionParser expressionParser() {
        return expressionParser;
    }

    public JtwigTagPropertyParser tagPropertyParser() {
        return tagPropertyParser;
    }

    public Rule rule() {
        return Test(true);
    }
    public abstract String keyword ();
}
