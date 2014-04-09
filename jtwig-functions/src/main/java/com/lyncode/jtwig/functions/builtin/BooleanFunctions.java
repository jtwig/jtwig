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

import com.lyncode.jtwig.functions.annotations.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.Parameter;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Map;

import static com.lyncode.jtwig.types.Undefined.UNDEFINED;
import static java.lang.Class.forName;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class BooleanFunctions { // Or Predicates
    @JtwigFunction(name = "constant")
    public boolean isConstant (@Parameter Object value, @Parameter String constant) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        int constantNamePosition = constant.lastIndexOf(".");
        String className = constant.substring(0, constantNamePosition);
        String constantName = constant.substring(constantNamePosition+1);

        return value.equals(forName(className).getDeclaredField(constantName).get(null));
    }

    @JtwigFunction(name = "defined")
    public boolean isDefined (@Parameter Object value) {
        return !UNDEFINED.equals(value);
    }

    @JtwigFunction(name = "divisable by")
    public boolean isDivisableBy (@Parameter Number value, @Parameter Number dividend) {
        double value1 = value.doubleValue();
        double value2 = dividend.doubleValue();

        return value1 % value2 == 0;
    }

    @JtwigFunction(name = "even")
    public boolean even (@Parameter int number) {
        return number % 2 == 0;
    }

    @JtwigFunction(name = "odd")
    public boolean odd (@Parameter int number) {
        return number % 2 == 1;
    }

    @JtwigFunction(name = "null")
    public boolean isNull (@Parameter Object input) {
        return input == null;
    }

    @JtwigFunction(name = "iterable")
    public boolean iterable (@Parameter Object input) {
        return input instanceof Iterable
                || input.getClass().isArray()
                || input instanceof Map;
    }

    @JtwigFunction(name = "empty")
    public boolean isEmpty (@Parameter Object input) {
        return anyOf(
                input,
                nullValue(Object.class),
                emptyList(),
                emptyMap(),
                notHasNext(),
                zeroValue()
        );
    }

    private boolean anyOf(Object input, Matcher<Object>... objectMatchers) {
        for (Matcher<Object> objectMatcher : objectMatchers) {
            if (objectMatcher.matches(input))
                return true;
        }
        return false;
    }


    private Matcher<Object> emptyMap() {
        return new BaseMatcher<Object>() {
            @Override
            public boolean matches(Object item) {
                if (item instanceof Map)
                    return ((Map) item).isEmpty();
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendDescriptionOf(empty());
            }
        };
    }

    private Matcher<Object> zeroValue() {
        return new BaseMatcher<Object>() {
            @Override
            public boolean matches(Object item) {
                return equalTo(0).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendDescriptionOf(equalTo(0));
            }
        };
    }

    private Matcher<Object> emptyList() {
        return new BaseMatcher<Object>() {
            @Override
            public boolean matches(Object item) {
                return empty().matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendDescriptionOf(empty());
            }
        };
    }

    private Matcher<Object> notHasNext() {
        return new BaseMatcher<Object>() {

            @Override
            public boolean matches(Object item) {
                if (item instanceof Iterable)
                    return !((Iterable) item).iterator().hasNext();
                else
                    return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("not has next");
            }
        };
    }
}
