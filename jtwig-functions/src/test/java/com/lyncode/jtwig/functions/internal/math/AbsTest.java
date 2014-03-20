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

package com.lyncode.jtwig.functions.internal.math;

import com.lyncode.jtwig.functions.exceptions.FunctionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AbsTest {
    private Abs underTest = new Abs();

    @Test
    public void testNegativeExecute() throws Exception {
        assertEquals(underTest.execute(-1), 1);
    }
    @Test
    public void testNegativeExecuteDouble() throws Exception {
        assertEquals(underTest.execute(-1.2), 1.2);
    }
    @Test
    public void testPositiveExecute() throws Exception {
        assertEquals(underTest.execute(1), 1);
    }
    @Test(expected = FunctionException.class)
    public void invalidNumberOfArguments() throws Exception {
        underTest.execute(1, 2);
    }
    @Test(expected = FunctionException.class)
    public void invalidNumberOfArguments2() throws Exception {
        underTest.execute();
    }
}
