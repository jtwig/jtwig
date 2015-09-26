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

import org.jtwig.JtwigModelMap;
import static org.jtwig.JtwigTemplate.inlineTemplate;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SortFilterTest {
    @Test
    public void generalTests() throws Exception {
        assertEquals("test", inlineTemplate("{{ 'test'|sort }}").render());
        assertEquals("1.21", inlineTemplate("{{ '1.21'|sort }}").render());
        assertEquals("2.2a", inlineTemplate("{{ '2.2a'|sort }}").render());
        assertEquals("1.2", inlineTemplate("{{ 1.2|sort }}").render());
        assertEquals("-1.2", inlineTemplate("{{ -1.2|sort }}").render());
        assertEquals("-1.2", inlineTemplate("{{ (-1.2)|sort }}").render());

        assertEquals("abc", inlineTemplate("{{ ['b','c','a']|sort|join }}").render());
        assertEquals("123", inlineTemplate("{{ {'b':'2','c':'3','a':'1'}|sort|join }}").render());
        assertEquals("abc", inlineTemplate("{{ {'b':'2','c':'3','a':'1'}|sort|keys|join }}").render());
        assertEquals("1", inlineTemplate("{{ true|sort }}").render());
        assertEquals("0", inlineTemplate("{{ false|sort }}").render());
        assertEquals("abc", inlineTemplate("{{ 'abc'|sort }}").render());
        assertEquals("1.5", inlineTemplate("{{ 1.5|sort }}").render());
        assertEquals("", inlineTemplate("{{ obj|sort }}").render(new JtwigModelMap().add("obj", new Object())));
    }
}