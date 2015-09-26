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
package org.jtwig.acceptance.extension.api.tokenparser;

import org.jtwig.JtwigTemplate;
import org.jtwig.compile.CompileContext;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.configuration.JtwigConfigurationBuilder;
import org.jtwig.content.api.Compilable;
import org.jtwig.content.api.Renderable;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ParseException;
import org.jtwig.extension.api.tokenparser.Tag;
import org.jtwig.loader.Loader;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.junit.Before;
import org.junit.Test;

public class TagTest {
    JtwigConfiguration config = JtwigConfigurationBuilder.defaultConfiguration();
    
    @Before
    public void before() {
        config.getExtensions().addTokenParser(BasicTag.class);
        config.getExtensions().addTokenParser(TagWithoutEndOrContent.class);
    }
    
    @Test
    public void generalTests() throws Exception {
        JtwigTemplate.inlineTemplate("{% test %}{% endtest %}", config).render();
        JtwigTemplate.inlineTemplate("{% open %}test{% if true %}hey{% endif %}", config).render();
    }
    @Test(expected = ParseException.class)
    public void openTagWithEndFails() throws Exception {
        JtwigTemplate.inlineTemplate("{% open %}{% endopen %}", config).render();
    }
    
    public static class BasicTag extends Tag {
        public BasicTag(Loader.Resource resource, JtwigContentParser content, JtwigBasicParser basic, JtwigExpressionParser expr, JtwigTagPropertyParser tag) {
            super(resource, content, basic, expr, tag);
        }
        @Override
        public String getKeyword() {
            return "test";
        }
        @Override
        public Compilable model(JtwigPosition pos) {
            return new Compilable() {
                @Override
                public Renderable compile(CompileContext context) throws CompileException {
                    return Renderable.NOOP;
                }
            };
        }
    }
    public static class TagWithoutEndOrContent extends BasicTag {
        public TagWithoutEndOrContent(Loader.Resource resource, JtwigContentParser content, JtwigBasicParser basic, JtwigExpressionParser expr, JtwigTagPropertyParser tag) {
            super(resource, content, basic, expr, tag);
        }
        @Override
        public String getKeyword() {
            return "open";
        }
        @Override
        public String getEndKeyword() {
            return null;
        }
    }
}