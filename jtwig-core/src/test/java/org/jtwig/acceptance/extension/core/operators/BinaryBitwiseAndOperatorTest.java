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
package org.jtwig.acceptance.extension.core.operators;

import org.jtwig.JtwigTemplate;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.configuration.JtwigConfigurationBuilder;
import org.jtwig.extension.core.CoreJtwigExtension;
import org.junit.Assert;
import org.junit.Test;

public class BinaryBitwiseAndOperatorTest {
    @Test
    public void generalTests() throws Exception {
        JtwigConfiguration config = JtwigConfigurationBuilder.newConfiguration()
                .withLogNonStrictMode(true)
                .build();
        config.getExtensions().addExtension(new CoreJtwigExtension(config));
        
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ 1 b-and null }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ 1 b-and var }}", config).render());
        Assert.assertEquals("1", JtwigTemplate.inlineTemplate("{{ 1 b-and 3 }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ 1 b-and 4 }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ 4 b-and 3 }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ 4 b-and 1 }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ 'a' b-and 1 }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ 1 b-and 'a' }}").render());
        
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ [] b-and 0 }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ [] b-and 1 }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ [] b-and 6 }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ [] b-and true }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ [] b-and false }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ [] b-and 'a' }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ [] b-and [] }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ [] b-and {} }}").render());

        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ {} b-and 0 }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ {} b-and 1 }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ {} b-and 6 }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ {} b-and true }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ {} b-and false }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ {} b-and 'a' }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ {} b-and [] }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ {} b-and {} }}").render());
    }
}