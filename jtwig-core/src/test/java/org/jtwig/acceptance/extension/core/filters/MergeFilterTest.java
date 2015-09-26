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

import java.util.Collections;
import java.util.TreeMap;
import org.jtwig.JtwigModelMap;
import static org.jtwig.JtwigTemplate.inlineTemplate;
import org.jtwig.exception.RenderException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class MergeFilterTest {
    
    @Test
    public void generalTests() throws Exception {
        assertEquals("ab12", inlineTemplate("{{ ['a','b']|merge([1,2])|join }}").render());
        assertEquals("ab01", inlineTemplate("{{ {'a':'1','b':'2'}|merge([1,2])|keys|join }}").render());
        assertEquals("1212", inlineTemplate("{{ {'a':'1','b':'2'}|merge([1,2])|join }}").render());
        assertEquals("abcd", inlineTemplate("{{ {'a':'1','b':'2'}|merge({'c':'3','d':'4'})|keys|join }}").render());
        assertEquals("1234", inlineTemplate("{{ {'a':'1','b':'2'}|merge({'c':'3','d':'4'})|join }}").render());
        assertEquals("abcd", inlineTemplate("{{ ['a','b']|merge(['c','d'],['e','f'])|join }}").render());
        assertEquals("abcd", inlineTemplate("{{ arr|merge(['c','d'])|join }}").render(new JtwigModelMap(Collections.singletonMap("arr", (Object)new String[]{"a","b"}))));
        assertEquals("abcd", inlineTemplate("{{ ['a','b']|merge(arr)|join }}").render(new JtwigModelMap(Collections.singletonMap("arr", (Object)new String[]{"c","d"}))));
        assertEquals("efcd", inlineTemplate("{{ map|merge(['e','f'])|join }}").render(new JtwigModelMap(Collections.singletonMap("map", (Object)new TreeMap<String, String>(){{
            put("0","a");
            put("1","b");
            put("2","c");
            put("3","d");
        }}))));
        assertEquals("abcd", inlineTemplate("{{ ['a','b']|merge({'a':'c','b':'d'})|join }}").render());
    }
    
    @Test(expected = RenderException.class)
    public void mergeStrings() throws Exception {
        inlineTemplate("{{ 'abc'|merge('def') }}").render();
    }
    
}