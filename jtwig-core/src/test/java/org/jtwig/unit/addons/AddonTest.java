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

package org.jtwig.unit.addons;

import org.jtwig.Environment;
import org.jtwig.addons.Addon;
import org.jtwig.addons.AddonModel;
import org.jtwig.loader.Loader;
import org.jtwig.loader.impl.EmptyLoader;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.parboiled.Parboiled;

public class AddonTest {
    @Test
    public void generalTests() throws Exception {
        Environment env = new Environment();
        Loader.Resource resource = new EmptyLoader.NoResource("test");
        TestAddon addon = Parboiled.createParser(TestAddon.class, resource, env);
        
        assertTrue(addon.basicParser() instanceof JtwigBasicParser);
        assertTrue(addon.expressionParser() instanceof JtwigExpressionParser);
        assertTrue(addon.tagPropertyParser() instanceof JtwigTagPropertyParser);
        assertNotNull(addon.startRule());
    }
    
    
    public static class TestAddon extends Addon {
        public TestAddon(Loader.Resource resource, Environment env) {
            super(resource, env);
        }

        @Override
        public AddonModel instance() {
            return new TestAddonModel();
        }

        @Override
        public String beginKeyword() {
            return "test";
        }

        @Override
        public String endKeyword() {
            return "endtest";
        }
        
        public static class TestAddonModel extends AddonModel<TestAddonModel> {
            
        }
    }
}