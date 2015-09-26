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

package org.jtwig.acceptance.extension.core.functions;

import org.jtwig.JtwigTemplate;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class RangeFunctionTest {
    
    @Test
    public void generalTests() throws Exception {
        assertEquals("123", JtwigTemplate.inlineTemplate("{{ range(1,3)|join }}").render());
        assertEquals("13", JtwigTemplate.inlineTemplate("{{ range(1,3,2)|join }}").render());
        assertEquals("abc", JtwigTemplate.inlineTemplate("{{ range('a', 'c')|join }}").render());
        assertEquals("AB", JtwigTemplate.inlineTemplate("{{ range('AA', 'BZ')|join }}").render());
    }
}