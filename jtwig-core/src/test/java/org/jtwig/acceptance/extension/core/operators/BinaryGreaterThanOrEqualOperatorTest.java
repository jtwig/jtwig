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

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BinaryGreaterThanOrEqualOperatorTest {
    
    @Test
    public void generalTests() throws Exception {
        assertEquals("1", JtwigTemplate.inlineTemplate("{{ 2 >= 2 }}").render());
        assertEquals("0", JtwigTemplate.inlineTemplate("{{ 2 >= 3 }}").render());
        assertEquals("0", JtwigTemplate.inlineTemplate("{{ 'test1' >= 'test2' }}").render());
        assertEquals("1", JtwigTemplate.inlineTemplate("{{ 'test2' >= 'test1' }}").render());
        assertEquals("1", JtwigTemplate.inlineTemplate("{{ 'test1' >= 'test1' }}").render());
        assertEquals("0", JtwigTemplate.inlineTemplate("{{ date('2014-12-12') >= date('2015-01-01') }}").render());
        assertEquals("1", JtwigTemplate.inlineTemplate("{{ date('2015-01-01') >= date('2014-12-12') }}").render());
        assertEquals("1", JtwigTemplate.inlineTemplate("{{ date('2015-01-01') >= date('2015-01-01') }}").render());
        JtwigModelMap model = new JtwigModelMap()
                .add("obj1", new Object())
                .add("obj2", new Object());
        assertEquals("0", JtwigTemplate.inlineTemplate("{{ obj1 >= obj2 }}").render(model));
        assertEquals("1", JtwigTemplate.inlineTemplate("{{ obj1 >= obj1 }}").render(model));
    }
    
}