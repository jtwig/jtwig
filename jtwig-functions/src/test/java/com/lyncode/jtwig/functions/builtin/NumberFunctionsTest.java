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

import com.lyncode.jtwig.functions.exceptions.FunctionException;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class NumberFunctionsTest {
    NumberFunctions underTest = new NumberFunctions();


    @Test
    public void numberFormat() throws FunctionException {
        assertEquals("1,234.57", underTest.numberFormat(1234.5678, 2, ".", ","));
    }
    @Test
    public void numberFormatWithoutGrouping() throws FunctionException {
        assertEquals("1234.57", underTest.numberFormat(1234.5678, 2, "."));
    }

    @Test
    public void numberFormatWithoutDecimalSeparator() throws FunctionException {
        assertEquals("1234.57", underTest.numberFormat(1234.5678, 2));
    }
    @Test
    public void numberFormatDefault() throws FunctionException {
        assertEquals("1234.568", underTest.numberFormat(1234.5678));
    }


    @Test
    public void range() throws FunctionException {
        List<Integer> list = underTest.range(1, 10, 2);
        assertThat(list, contains(1, 3, 5, 7, 9));
    }
    @Test
    public void rangeDefault() throws FunctionException {
        List<Integer> list = underTest.range(1, 3);
        assertThat(list, contains(1, 2, 3));
    }
    @Test
    public void rangeChars() throws FunctionException {
        List<Character> list = underTest.range('A', 'D');
        assertThat(list, contains('A', 'B', 'C', 'D'));
    }

    @Test(expected = FunctionException.class)
    public void rangeInvalidStep() throws FunctionException {
        underTest.range(1, 3, 0);
    }
    @Test
    public void rangeHugeStep() throws FunctionException {
        List<Integer> list = underTest.range(1, 3, 50);
        assertThat(list, contains(1));
    }
    @Test
    public void rangeNegativeStepWithPositiveProgression() throws FunctionException {
        List<Integer> list = underTest.range(1, 3, -1);
        assertThat(list, contains(1, 2, 3));
    }
}
