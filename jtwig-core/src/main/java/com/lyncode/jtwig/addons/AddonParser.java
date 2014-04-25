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

package com.lyncode.jtwig.addons;

import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.parser.parboiled.JtwigBaseParser;
import com.lyncode.jtwig.parser.parboiled.JtwigBasicParser;
import com.lyncode.jtwig.parser.parboiled.JtwigExpressionParser;
import com.lyncode.jtwig.parser.parboiled.JtwigTagPropertyParser;
import com.lyncode.jtwig.resource.JtwigResource;
import org.parboiled.Parboiled;
import org.parboiled.Rule;

public abstract class AddonParser extends JtwigBaseParser<Addon> {
    final JtwigBasicParser basicParser;
    final JtwigExpressionParser expressionParser;
    final JtwigTagPropertyParser tagPropertyParser;

    public AddonParser(JtwigResource resource, ParserConfiguration configuration) {
        super(resource);
        basicParser = Parboiled.createParser(JtwigBasicParser.class, configuration);
        expressionParser = Parboiled.createParser(JtwigExpressionParser.class, resource, configuration);
        tagPropertyParser = Parboiled.createParser(JtwigTagPropertyParser.class, configuration);
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

    public Rule startRule() {
        return Optional(push(instance()));
    }

    public abstract Addon instance ();
    public abstract String beginKeyword ();
    public abstract String endKeyword ();
}
