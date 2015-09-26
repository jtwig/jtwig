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
import org.junit.Assert;
import org.junit.Test;

public class BinaryBitwiseXorOperatorTest {
    @Test
    public void generalTests() throws Exception {
        Assert.assertEquals("2", JtwigTemplate.inlineTemplate("{{ 1 b-xor 3 }}").render());
        Assert.assertEquals("5", JtwigTemplate.inlineTemplate("{{ 1 b-xor 4 }}").render());
        Assert.assertEquals("7", JtwigTemplate.inlineTemplate("{{ 4 b-xor 3 }}").render());
        Assert.assertEquals("5", JtwigTemplate.inlineTemplate("{{ 4 b-xor 1 }}").render());
        Assert.assertEquals("1", JtwigTemplate.inlineTemplate("{{ 'a' b-xor 1 }}").render());
        Assert.assertEquals("1", JtwigTemplate.inlineTemplate("{{ 1 b-xor 'a' }}").render());
        
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ [] b-xor 0 }}").render());
        Assert.assertEquals("1", JtwigTemplate.inlineTemplate("{{ [] b-xor 1 }}").render());
        Assert.assertEquals("6", JtwigTemplate.inlineTemplate("{{ [] b-xor 6 }}").render());
        Assert.assertEquals("1", JtwigTemplate.inlineTemplate("{{ [] b-xor true }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ [] b-xor false }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ [] b-xor 'a' }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ [] b-xor [] }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ [] b-xor {} }}").render());

        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ {} b-xor 0 }}").render());
        Assert.assertEquals("1", JtwigTemplate.inlineTemplate("{{ {} b-xor 1 }}").render());
        Assert.assertEquals("6", JtwigTemplate.inlineTemplate("{{ {} b-xor 6 }}").render());
        Assert.assertEquals("1", JtwigTemplate.inlineTemplate("{{ {} b-xor true }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ {} b-xor false }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ {} b-xor 'a' }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ {} b-xor [] }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ {} b-xor {} }}").render());
        
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ null b-xor 0 }}").render());
        Assert.assertEquals("1", JtwigTemplate.inlineTemplate("{{ null b-xor 1 }}").render());
        Assert.assertEquals("6", JtwigTemplate.inlineTemplate("{{ null b-xor 6 }}").render());
        Assert.assertEquals("1", JtwigTemplate.inlineTemplate("{{ null b-xor true }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ null b-xor false }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ null b-xor 'a' }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ null b-xor [] }}").render());
        Assert.assertEquals("0", JtwigTemplate.inlineTemplate("{{ null b-xor {} }}").render());
    }
}