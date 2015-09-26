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

package org.jtwig.extension.api.tokenparser;

import org.jtwig.content.api.Compilable;
import org.jtwig.loader.Loader;
import org.jtwig.parser.parboiled.JtwigBaseParser;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.parboiled.Rule;

public abstract class TokenParser extends JtwigBaseParser<Compilable> {
    public final JtwigContentParser content;
    public final JtwigBasicParser basic;
    public final JtwigExpressionParser expr;
    public final JtwigTagPropertyParser tag;

    public TokenParser(Loader.Resource resource, JtwigContentParser content,
            JtwigBasicParser basic, JtwigExpressionParser expr,
            JtwigTagPropertyParser tag) {
        super(resource);
        this.content = content;
        this.basic = basic;
        this.expr = expr;
        this.tag = tag;
    }
    
    public abstract Rule rule();
    
    public String[] keywords() {
        return new String[0];
    }
    
    public boolean addToContent() {
        return true;
    }
    public boolean addToTracker() {
        return false;
    }
    
    public JtwigContentParser getContentParser() {
        return content;
    }
    public JtwigBasicParser getBasicParser() {
        return basic;
    }
    public JtwigExpressionParser getExpressionParser() {
        return expr;
    }
    public JtwigTagPropertyParser getTagPropertyParser() {
        return tag;
    }
    
}