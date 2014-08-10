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
import com.lyncode.jtwig.functions.repository.model.Function;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

import static com.lyncode.jtwig.functions.repository.model.Function.functionFrom;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class FunctionTest {
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
        Function stringReference = functionFrom(stringMethod).on(instance);
        Function objectReference = functionFrom(objectMethod).on(instance);

        // String less than Object (first to appear in a list)
        assertTrue(objectReference.compareTo(stringReference) > 0);
    }

    @Test
    public void fewerParametersLast() throws Exception {
        Function stringReference = functionFrom(stringMethod).on(instance);
        Function doubleStringReference = functionFrom(doubleStringMethod).on(instance);

        // String less than Double String (first to appear in a list)
        assertTrue(doubleStringReference.compareTo(stringReference) < 0);
    }

    @Test
    public void parameterTypes() throws Exception {
        Function reference = Function.functionFrom(getMethod(String.class)).on(instance);
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
