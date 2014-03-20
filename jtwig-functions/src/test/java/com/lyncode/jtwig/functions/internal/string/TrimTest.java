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

package com.lyncode.jtwig.functions.internal.string;

import com.lyncode.jtwig.functions.exceptions.FunctionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TrimTest {
    private Trim underTest = new Trim();

    @Test
    public void shouldGiveTrimmedString() throws Exception {
        assertEquals(underTest.execute(" abc "), "abc");
    }

    @Test
    public void shouldGiveStringInUppercase2() throws Exception {
        assertEquals(underTest.execute("abc"), "abc");
    }

    @Test(expected = FunctionException.class)
    public void invalidNumberOfArguments() throws Exception {
        underTest.execute("abc", "a");
    }
    @Test
    public void nullPointer() throws Exception {
        assertNull(underTest.execute((Object) null));
    }
}
