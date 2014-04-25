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

import java.util.ArrayList;
import java.util.HashMap;

import static com.lyncode.jtwig.types.Undefined.UNDEFINED;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BooleanFunctionsTest {
    public static final String STATIC = "value";
    private BooleanFunctions underTest = new BooleanFunctions();

    @Test
    public void even() throws Exception {
        assertTrue(underTest.even(2));
    }
    @Test
    public void evenFalse() throws Exception {
        assertFalse(underTest.even(3));
    }

    @Test
    public void odd() throws Exception {
        assertTrue(underTest.odd(3));
    }
    @Test
    public void oddFalse() throws Exception {
        assertFalse(underTest.odd(2));
    }

    @Test
    public void constant() throws Exception {
        assertTrue(underTest.isEqualToConstant("value", getClass().getName() + ".STATIC"));
    }

    @Test
    public void defined() throws Exception {
        assertTrue(underTest.isDefined("value"));
    }

    @Test
    public void notDefined() throws Exception {
        assertFalse(underTest.isDefined(UNDEFINED));
    }

    @Test
    public void divisable() throws Exception {
        assertTrue(underTest.isDivisableBy(2, 1));
    }

    @Test
    public void isNull() throws Exception {
        assertTrue(underTest.isNull(null));
    }

    @Test
    public void isIterable() throws Exception {
        assertTrue(underTest.iterable(new ArrayList<>()));
        assertTrue(underTest.iterable(new Object[0]));
        assertTrue(underTest.iterable(new HashMap<>()));
    }

    @Test
    public void isEmpty() throws Exception {
        assertTrue(underTest.isEmpty(new ArrayList<>()));
        assertTrue(underTest.isEmpty(new HashMap<>()));
        assertTrue(underTest.isEmpty(null));
        assertTrue(underTest.isEmpty(0));
    }

}
