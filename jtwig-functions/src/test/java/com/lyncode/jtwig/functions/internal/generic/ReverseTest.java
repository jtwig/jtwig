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

package com.lyncode.jtwig.functions.internal.generic;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class ReverseTest {
    private Reverse underTest = new Reverse();

    @Test
    public void testExecuteString() throws Exception {
        assertEquals("cba", underTest.execute("abc"));
    }

    @Test
    public void testExecuteList() throws Exception {
        List result = (List) underTest.execute(asList("a", "b", "c"));
        assertEquals("c", result.get(0));
        assertEquals("b", result.get(1));
        assertEquals("a", result.get(2));
    }
}
