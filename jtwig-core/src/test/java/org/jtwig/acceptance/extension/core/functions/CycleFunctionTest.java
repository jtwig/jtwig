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

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.jtwig.JtwigTemplate.inlineTemplate;
import static org.hamcrest.Matchers.*;
import org.jtwig.JtwigModelMap;
import org.jtwig.exception.RenderException;

public class CycleFunctionTest {
    @Test
    public void generalTests() throws Exception {
        Collection<String> list = Arrays.asList("apple", "banana", "orange");
        Collection<String> set = new LinkedHashSet<>(list);
        Map<String, String> map = new LinkedHashMap<String, String>(){{
            put("a", "apple");
            put("b", "banana");
            put("o", "orange");
        }};
        JtwigModelMap model = new JtwigModelMap()
                .add("list", list)
                .add("set", set)
                .add("map", map)
                .add("emptyList", Collections.EMPTY_LIST)
                .add("emptySet", Collections.EMPTY_SET);
        
        assertThat(inlineTemplate("{{ cycle(list, 1) }}").render(model),
                is(equalTo("banana")));
        assertThat(inlineTemplate("{{ cycle(list, 5) }}").render(model),
                is(equalTo("orange")));
        assertThat(inlineTemplate("{{ cycle(set, 1) }}").render(model),
                is(equalTo("banana")));
        assertThat(inlineTemplate("{{ cycle(set, 5) }}").render(model),
                is(equalTo("orange")));
        assertThat(inlineTemplate("{{ cycle(map, 1) }}").render(model),
                is(equalTo("banana")));
        assertThat(inlineTemplate("{{ cycle(map, 5) }}").render(model),
                is(equalTo("orange")));
        assertThat(inlineTemplate("{{ cycle(emptySet, 2) }}").render(model),
                isEmptyString());
        assertThat(inlineTemplate("{{ cycle(emptyList, 2) }}").render(model),
                isEmptyString());
        assertThat(inlineTemplate("{{ cycle('abc', 2) }}").render(model),
                is(equalTo("abc")));
    }
    
    @Test(expected = RenderException.class)
    public void testFailureOnMissingArgument() throws Exception {
        inlineTemplate("{{ cycle(list) }}").render();
    }
    
    @Test(expected = RenderException.class)
    public void testFailureOnNonNumericPosition() throws Exception {
        inlineTemplate("{{ cycle(list, 'a') }}").render();
    }
}