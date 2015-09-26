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
package org.jtwig.acceptance.addons.tag;

import org.jtwig.JtwigTemplate;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.configuration.JtwigConfigurationBuilder;
import org.junit.Assert;
import org.junit.Test;

public class TagAddonTest {
    @Test
    public void generalTests() throws Exception {
        JtwigConfiguration config = JtwigConfigurationBuilder.defaultConfiguration();
        config.getExtensions().addAddon(TestTagAddon.class);
        
        Assert.assertEquals("", JtwigTemplate.inlineTemplate("{% test %}{% endtest %}", config).render());
    }
}