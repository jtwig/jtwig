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

import com.lyncode.jtwig.functions.exceptions.FunctionException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Vitali Carbivnicii
 * Date: 2014-03-25
 * Time: 23:32
 */
public class RangeTest {

    private Range underTest = new Range();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testDefault() throws FunctionException {

        List<Integer> target = asList(1, 2, 3);
        assertEquals(target, underTest.execute(1, 3));

        target = asList(10, 8, 6, 4, 2, 0, -2, -4);
        assertEquals(target, underTest.execute(10, -5, 2));

        target = asList(0, 3, 6, 9, 12);
        assertEquals(target, underTest.execute(0, 13, 3));

        target = asList(10);
        assertEquals(target, underTest.execute(10, 10, 10));

    }

    @Test
    public void testInvalidArguments1() throws FunctionException {

        exception.expect(FunctionException.class);
        exception.expectMessage("Invalid number of arguments, it should be between <2> and <3>");
        underTest.execute(1);

    }

    @Test
    public void testInvalidArguments2() throws FunctionException {

        exception.expect(FunctionException.class);
        exception.expectMessage("Invalid number of arguments, it should be between <2> and <3>");
        underTest.execute(1, 2, 3, 4);

    }

    @Test
    public void testInvalidArguments3() throws FunctionException {

        exception.expect(FunctionException.class);
        exception.expectMessage("Invalid argument 2 (3). It should an instance of java.lang.Integer");
        underTest.execute(1, 2, "3");

    }

    @Test
    public void testInvalidArguments4() throws FunctionException {

        exception.expect(FunctionException.class);
        exception.expectMessage("Step must not be 0");
        underTest.execute(1, 2, 0);

    }

    @Test
    public void testInvalidArguments5() throws FunctionException {

        exception.expect(FunctionException.class);
        exception.expectMessage("Step is too big");
        underTest.execute(1, 10, 20);

    }

    @Test
    public void testInvalidArguments6() throws FunctionException {

        exception.expect(FunctionException.class);
        exception.expectMessage("Step is too big");
        underTest.execute(10, 0, 20);

    }

}
