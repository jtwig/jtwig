/**
 * Copyright 2012 Lyncode
 *
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

package com.lyncode.jtwig.functions.internal.map;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class MapKeysTest {
    private MapKeys underTest = new MapKeys();

    @Test
    public void testExecute() throws Exception {
        Map<String, String> map = new TreeMap<String, String>();
        map.put("a", "a");
        map.put("b", "a");

        List<String> list = (List<String>) underTest.execute(map);
        assertThat(list, hasItem("a"));
        assertThat(list, hasItem("b"));
    }
}
