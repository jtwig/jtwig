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
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class RoundFilterTest {
    
    @Test
    public void generalTests() throws Exception {
        assertEquals("5", JtwigTemplate.inlineTemplate("{{ 5|round }}").render());
        assertEquals("0", JtwigTemplate.inlineTemplate("{{ 'test'|round }}").render());
        assertEquals("2", JtwigTemplate.inlineTemplate("{{ '1.5'|round }}").render());
        assertEquals("2", JtwigTemplate.inlineTemplate("{{ '1.2'|round(0, 'ceil') }}").render());
        assertEquals("1", JtwigTemplate.inlineTemplate("{{ '1.7'|round(0, 'floor') }}").render());
        assertEquals("2", JtwigTemplate.inlineTemplate("{{ '2.2a'|round }}").render());
        assertEquals("1", JtwigTemplate.inlineTemplate("{{ 1.2|round }}").render());
        assertEquals("-1", JtwigTemplate.inlineTemplate("{{ -1.2|round }}").render());
        assertEquals("1.2", JtwigTemplate.inlineTemplate("{{ 1.2345|round(1) }}").render());
        assertEquals("1.3", JtwigTemplate.inlineTemplate("{{ 1.2345|round(1, 'ceil') }}").render());
        assertEquals("1.235", JtwigTemplate.inlineTemplate("{{ 1.2345|round(3) }}").render());
        assertEquals("1.234", JtwigTemplate.inlineTemplate("{{ 1.2345|round(3, 'floor') }}").render());
        assertEquals("", JtwigTemplate.inlineTemplate("{{ ['a','b']|round }}").render());
        assertEquals("", JtwigTemplate.inlineTemplate("{{ {'a':'1','b':'2'}|round }}").render());
    }
    
}