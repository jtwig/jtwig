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

package org.jtwig.functions.builtin;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ObjectFunctionsTest {
    ObjectFunctions underTest = new ObjectFunctions();
    
    @Test
    public void toDoubleTests() throws Exception {
        assertEquals(2.1D, underTest.toDouble(2.1F), 0);
        assertEquals(2.1D, underTest.toDouble(2.1D), 0);
        assertEquals(2D, underTest.toDouble(2), 0);
        assertEquals(2.1D, underTest.toDouble("2.1"), 0);
    }
    
    @Test
    public void toIntTests() throws Exception {
        assertEquals(2, (int)underTest.toInt(2D));
        assertEquals(2, (int)underTest.toInt(2F));
        assertEquals(2, (int)underTest.toInt(2));
        assertEquals(2, (int)underTest.toInt("2"));
    }
    
}