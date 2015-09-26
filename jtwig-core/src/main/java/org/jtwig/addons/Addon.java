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

package org.jtwig.addons;

import org.jtwig.Environment;
import org.jtwig.loader.Loader;
import org.jtwig.parser.parboiled.JtwigBaseParser;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;

@Deprecated
public abstract class Addon extends JtwigBaseParser<AddonModel> {
    final JtwigBasicParser basicParser;
    final JtwigExpressionParser expressionParser;
    final JtwigTagPropertyParser tagPropertyParser;

    public Addon(Loader.Resource resource, Environment env) {
        super(resource);
        basicParser = env.getBasicParser();
        expressionParser = Parboiled.createParser(JtwigExpressionParser.class, resource, env);
        tagPropertyParser = env.getTagPropertyParser();
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

    public abstract AddonModel instance ();
    public abstract String beginKeyword ();
    public abstract String endKeyword ();
}
