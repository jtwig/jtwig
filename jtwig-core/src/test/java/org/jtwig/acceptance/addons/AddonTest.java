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

package org.jtwig.acceptance.addons;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import org.jtwig.Environment;
import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.jtwig.addons.Addon;
import org.jtwig.addons.AddonModel;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.configuration.JtwigConfigurationBuilder;
import org.jtwig.loader.Loader;
import org.junit.Test;

public class AddonTest {
    @Test
    public void testSpaceTag() throws Exception {;
        JtwigConfiguration config = JtwigConfigurationBuilder.defaultConfiguration();
        config.getExtensions().addAddon(TestAddon.class);

        String result = JtwigTemplate
            .inlineTemplate("{% form %}hello{% endform %}", config)
            .render();

        assertThat(result, equalTo("hello"));
    }
    
    public static class TestAddon extends Addon {

        public TestAddon(Loader.Resource resource, Environment env) {
            super(resource, env);
        }

        @Override
        public AddonModel instance() {
            return new AddonModel() {
            };
        }

        @Override
        public String beginKeyword() {
            return "form";
        }

        @Override
        public String endKeyword() {
            return "endform";
        }
        
    }
}