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

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class SliceTest {
    private Slice underTest = new Slice();

    @Test
    public void testExecuteList() throws Exception {
        List result = (List) underTest.execute(asList(1, 2, 3, 4, 5), 1, 2);
        assertEquals(2, result.get(0));
        assertEquals(3, result.get(1));
        assertEquals(2, result.size());
    }
    @Test
    public void testExecuteString() throws Exception {
        String result = (String) underTest.execute("abc", 1, 2);
        assertEquals("bc", result);
    }
    @Test
    public void testExecuteStringIncomplete() throws Exception {
        String result = (String) underTest.execute("ab", 1, 2);
        assertEquals("b", result);
    }
    @Test
    public void testExecuteStringIncompleteBegin() throws Exception {
        String result = (String) underTest.execute("ab", 3, 2);
        assertEquals("", result);
    }
}
