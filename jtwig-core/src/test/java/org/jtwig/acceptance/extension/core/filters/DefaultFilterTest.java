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

package org.jtwig.acceptance.extension.core.filters;

import org.jtwig.JtwigTemplate;
import org.jtwig.exception.RenderException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DefaultFilterTest {
    @Test
    public void generalTest() throws Exception {
        assertEquals("a", JtwigTemplate.inlineTemplate("{{ null|default('a') }}").render());
        assertEquals("1", JtwigTemplate.inlineTemplate("{{ null|default(1) }}").render());
        assertEquals("a", JtwigTemplate.inlineTemplate("{{ 'a'|default(1) }}").render());
        assertEquals("1", JtwigTemplate.inlineTemplate("{{ ''|default(1) }}").render());
        assertEquals("1", JtwigTemplate.inlineTemplate("{{ var|default(1) }}").render());
        assertEquals("1", JtwigTemplate.inlineTemplate("{{ var.val|default(1) }}").render());
    }
    
    @Test(expected = RenderException.class)
    public void testWithoutDefaultArg() throws Exception {
        JtwigTemplate.inlineTemplate("{{ null|default }}").render();
    }
}