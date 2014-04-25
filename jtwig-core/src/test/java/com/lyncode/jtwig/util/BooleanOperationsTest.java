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

package com.lyncode.jtwig.util;

import com.lyncode.jtwig.types.Undefined;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static com.lyncode.jtwig.util.BooleanOperations.*;
import static org.junit.Assert.*;

public class BooleanOperationsTest {
    @Test
    public void andTest() throws Exception {
        assertEquals(false, and(true, false));
    }

    @Test
    public void andTrueTest() throws Exception {
        assertEquals(true, and(true, true));
    }

    @Test
    public void orTest() throws Exception {
        assertEquals(true, or(true, false));
    }

    @Test
    public void orFalseTest() throws Exception {
        assertEquals(false, or(false, false));
    }

    @Test
    public void notTest() throws Exception {
        assertEquals(false, not(true));
    }

    @Test
    public void isTrueTest() throws Exception {
        assertTrue(isTrue(1));
        assertFalse(isTrue(0));
        assertFalse(isTrue(null));
        assertFalse(isTrue(new ArrayList<>()));
        assertFalse(isTrue(new Object[0]));
        assertFalse(isTrue(Undefined.UNDEFINED));
        assertFalse(isTrue(0.0));
        assertFalse(isTrue(new HashMap<>()));
    }
}
