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

package org.jtwig.acceptance.extension.core.tests;

import org.jtwig.JtwigTemplate;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class EmptyTestTest {
    
    @Test
    public void generalTests() throws Exception {
        assertEquals("1", JtwigTemplate.inlineTemplate("{{ [] is empty }}").render());
        assertEquals("1", JtwigTemplate.inlineTemplate("{{ {} is empty }}").render());
        assertEquals("1", JtwigTemplate.inlineTemplate("{{ null is empty }}").render());
        assertEquals("1", JtwigTemplate.inlineTemplate("{{ 0 is empty }}").render());
        assertEquals("0", JtwigTemplate.inlineTemplate("{{ ['a'] is empty }}").render());
        assertEquals("0", JtwigTemplate.inlineTemplate("{{ {'a':'1'} is empty }}").render());
    }
    
}
