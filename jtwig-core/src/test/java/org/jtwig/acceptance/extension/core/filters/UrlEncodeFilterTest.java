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
import org.jtwig.JtwigTemplate;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UrlEncodeFilterTest {
    
    @Test
    public void generalTests() throws Exception {
        assertEquals("%20test%20", JtwigTemplate.inlineTemplate("{{ ' test '|url_encode }}").render());
        assertEquals("1.21", JtwigTemplate.inlineTemplate("{{ '1.21'|url_encode }}").render());
        assertEquals("2.2a", JtwigTemplate.inlineTemplate("{{ '2.2a'|url_encode }}").render());
        assertEquals("1.2", JtwigTemplate.inlineTemplate("{{ 1.2|url_encode }}").render());
        assertEquals("-1.2", JtwigTemplate.inlineTemplate("{{ (-1.2)|url_encode }}").render());

        assertEquals("0=a&1=b&2=c", JtwigTemplate.inlineTemplate("{{ ['a','b','c']|url_encode }}").render());
        assertEquals("a=1&b=2&c=3", JtwigTemplate.inlineTemplate("{{ {'a':'1','b':'2','c':'3'}|url_encode }}").render());
        assertEquals("a=1&b=2&c=3", JtwigTemplate.inlineTemplate("{{ {'a':'1','b':'2','c':'3'}|url_encode }}").render());
        assertEquals("", JtwigTemplate.inlineTemplate("{{ obj|url_encode }}").render());
        assertEquals("", JtwigTemplate.inlineTemplate("{{ obj|url_encode }}").render(new JtwigModelMap().add("obj", new Object())));
    }
}