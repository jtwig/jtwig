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

package com.lyncode.jtwig.functions.repository;

import com.lyncode.jtwig.functions.annotations.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.Parameter;
import com.lyncode.jtwig.functions.exceptions.FunctionNotFoundException;
import com.lyncode.jtwig.functions.parameters.GivenParameters;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
<<<<<<< HEAD
import static org.mockito.Mockito.mock;
=======
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
>>>>>>> master

public class FunctionResolverTest {
    private FunctionResolver underTest = new FunctionResolver();

    @Test(expected = FunctionNotFoundException.class)
    public void throwExceptionIfNotFound() throws Exception {
        underTest.get("some", mock(GivenParameters.class));
    }

    @Test
    public void mustReturnBuiltInFunctions() throws Exception {
        assertNotNull(underTest.get("null", parameters(new Object[]{null})));
    }

    @Test
    public void shouldWorkWithNew() throws Exception {
        TestClass test = new TestClass();
        underTest.store(test);
        underTest.get("test", parameters("hello")).execute();
    }

    @Test(expected = FunctionNotFoundException.class)
    public void shouldNotWorkWithWrongNumberOfArguments() throws Exception {
        TestClass test = new TestClass();
        underTest.store(test);
        underTest.get("test", parameters("one", "two")).execute();
    }

    private GivenParameters parameters(Object... parameters) {
<<<<<<< HEAD
        return new GivenParameters().add(parameters);
=======
        return new GivenParameters().addArray(parameters);
>>>>>>> master
    }

    public static class TestClass {
        @JtwigFunction(name = "test")
        public String test (@Parameter String input) {
            return input;
        }
        @JtwigFunction(name = "test")
        public String test (@Parameter Object input) {
            return input.toString();
        }
    }
}
