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

public class StripTagsFilterTest {
    
    @Test
    public void generalTests() throws Exception {
        assertEquals("<p>Hi Joao</p>", JtwigTemplate.inlineTemplate("{{ '<a><p>Hi Joao</p></a>'|striptags('p') }}").render());
        assertEquals("test", JtwigTemplate.inlineTemplate("{{ 'te<br>st'|striptags }}").render());
        assertEquals("1.21", JtwigTemplate.inlineTemplate("{{ '1.21'|striptags }}").render());
        assertEquals("2.2a", JtwigTemplate.inlineTemplate("{{ '2.2a'|striptags }}").render());
        assertEquals("1.2", JtwigTemplate.inlineTemplate("{{ 1.2|striptags }}").render());
        assertEquals("-1.2", JtwigTemplate.inlineTemplate("{{ (-1.2)|striptags }}").render());

        assertEquals("", JtwigTemplate.inlineTemplate("{{ ['a','b','c']|striptags }}").render());
        assertEquals("", JtwigTemplate.inlineTemplate("{{ {'a':'1','b':'2','c':'3'}|striptags }}").render());
        assertEquals("", JtwigTemplate.inlineTemplate("{{ {'a':'1','b':'2','c':'3'}|striptags }}").render());
        assertEquals("", JtwigTemplate.inlineTemplate("{{ obj|striptags }}").render());
    }
}