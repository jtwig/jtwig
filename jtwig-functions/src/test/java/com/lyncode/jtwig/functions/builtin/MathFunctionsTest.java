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

import static org.junit.Assert.assertEquals;

public class MathFunctionsTest {
    MathFunctions underTest = new MathFunctions();


    @Test
    public void absNegativeExecute() throws Exception {
        assertEquals(underTest.abs(-1), (Integer) 1);
    }
    @Test
    public void absNegativeExecuteDouble() throws Exception {
        assertEquals(underTest.abs(-1.2), (Double) 1.2);
    }
    @Test
    public void absPositiveExecute() throws Exception {
        assertEquals(underTest.abs(1), (Integer) 1);
    }

    @Test
    public void roundExecuteCommon() throws Exception {
        assertEquals(1, underTest.round(1.01));
    }

    @Test
    public void roundExecuteFloor() throws Exception {
        assertEquals(1, underTest.round(1.7, "floor"));
    }
    @Test
    public void roundExecuteCeil() throws Exception {
        assertEquals(2, underTest.round(1.01, "ceil"));
    }
}
