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

import com.lyncode.jtwig.functions.annotations.Parameter;
import com.lyncode.jtwig.functions.exceptions.FunctionNotFoundException;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class FunctionReferenceTest {
    private TestClass instance = new TestClass();
    private Method stringMethod = getMethod(String.class);
    private Method objectMethod = getMethod(Object.class);
    private Method doubleStringMethod = getMethod(String.class, String.class);

    private Method getMethod(Class<?>... types) {
        try {
            return TestClass.class.getDeclaredMethod("identity", types);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }



    @Test
    public void stringParameterFirstThanObject() throws Exception {
        FunctionReference stringReference = new FunctionReference(stringMethod, instance);
        FunctionReference objectReference = new FunctionReference(objectMethod, instance);

        // String less than Object (first to appear in a list)
        assertTrue(objectReference.compareTo(stringReference) > 0);
    }

    @Test
    public void fewerParametersFirst() throws Exception {
        FunctionReference stringReference = new FunctionReference(stringMethod, instance);
        FunctionReference doubleStringReference = new FunctionReference(doubleStringMethod, instance);

        // String less than Double String (first to appear in a list)
        assertTrue(doubleStringReference.compareTo(stringReference) > 0);
    }

    @Test
    public void executeCallsTheInvokeMethod() throws Exception {
        Method method = getMethod(String.class);
        FunctionReference reference = new FunctionReference(method, instance);
        Object[] arguments = {"one"};
        reference.execute(arguments);

        assertEquals("one", method.invoke(instance, arguments));
    }

    @Test
    public void parameterTypes() throws Exception {
        FunctionReference reference = new FunctionReference(getMethod(String.class), instance);
        List<Class<?>> list = reference.getParameterTypesWithAnnotation(Parameter.class);

        assertThat(list.size(), equalTo(1));
        assertEquals(String.class, list.get(0));
    }

    public static class TestClass {
        public String identity (@Parameter String input) {
            return input;
        }
        public String identity (String input, String other) {
            return input;
        }
        public Object identity (Object input) {
            return input;
        }
    }
}
