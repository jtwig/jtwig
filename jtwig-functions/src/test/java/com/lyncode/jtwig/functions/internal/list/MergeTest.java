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

package com.lyncode.jtwig.functions.internal.list;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class MergeTest {
    private Merge underTest = new Merge();

    @Test
    public void testExecuteList() throws Exception {
        List result = (List) underTest.execute(asList(new String[]{"a", "b"}), asList(new Integer[]{1, 2}));
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals(1, result.get(2));
        assertEquals(2, result.get(3));
    }
    @Test
    public void testExecuteArrays() throws Exception {
        Object firstHalf = (Object) new String[]{"a", "b"};
        Object secondHalf = (Object) new Integer[]{1, 2};
        Object[] result = (Object[]) underTest.execute(firstHalf, secondHalf);
        assertEquals("a", result[0]);
        assertEquals("b", result[1]);
        assertEquals(1, result[2]);
        assertEquals(2, result[3]);
    }
    @Test
    public void testExecuteMaps() throws Exception {
        Object firstHalf = (Object) new HashMap<String, Object>(){{
            put("one", 1);
            put("two", 2);
        }};
        Object secondHalf = (Object) new HashMap<String, Object>(){{
            put("three", 3);
            put("four", 4);
        }};
        Map result = (Map) underTest.execute(firstHalf, secondHalf);
        assertEquals(1, result.get("one"));
        assertEquals(2, result.get("two"));
        assertEquals(3, result.get("three"));
        assertEquals(4, result.get("four"));
    }
}
