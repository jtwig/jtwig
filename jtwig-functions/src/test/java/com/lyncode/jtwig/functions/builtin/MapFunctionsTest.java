/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.jtwig.functions.builtin;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertEquals;

public class MapFunctionsTest {
    MapFunctions underTest = new MapFunctions();



    @Test
    public void first() throws Exception {
        assertEquals(underTest.first(new TreeMap<String, Object>() {{
            put("a", 1);
            put("b", 2);
        }}), 1);
    }


    @Test
    public void last() throws Exception {
        assertEquals(underTest.last(new TreeMap<String, Object>() {{
            put("a", 1);
            put("b", 2);
        }}), 2);
    }


    @Test
    public void lenght() throws Exception {
        Integer length = underTest.length(new HashMap<String, Object>() {{
            put("a", null);
            put("b", null);
        }});
        assertThat(length, is(2));
    }


    @Test
    public void mapKeys() throws Exception {
        Map<String, String> map = new TreeMap<String, String>();
        map.put("a", "a");
        map.put("b", "a");

        Set<String> list = (Set<String>) underTest.keys(map);
        assertThat(list, hasItem("a"));
        assertThat(list, hasItem("b"));
    }
}
