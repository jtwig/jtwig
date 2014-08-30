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
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.functions.util.CastMatcher;
import com.lyncode.jtwig.types.Undefined;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.AnyOf;

import java.util.Collection;
import java.util.Map;

import static com.lyncode.jtwig.types.Undefined.UNDEFINED;
import static java.lang.Class.forName;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class BooleanFunctions { // Or Predicates

    @JtwigFunction(name = "constant")
    public boolean isEqualToConstant(@Parameter Object value, @Parameter String constant) throws FunctionException {
        int constantNamePosition = constant.lastIndexOf(".");
        if (constantNamePosition == -1)
            throw new FunctionException(String.format("Invalid constant specified '%s'", constant));

        String className = constant.substring(0, constantNamePosition);
        String constantName = constant.substring(constantNamePosition + 1);

        try {
            return value.equals(forName(className).getDeclaredField(constantName).get(null));
        } catch (Exception e) {
            throw new FunctionException(String.format("Constant '%s' does not exist", constant));
        }
    }

    @JtwigFunction(name = "defined")
    public boolean isDefined(@Parameter Object value) {
        return !UNDEFINED.equals(value);
    }

    @JtwigFunction(name = "divisable by")
    public boolean isDivisableBy(@Parameter Number value, @Parameter Number dividend) {
        double value1 = value.doubleValue();
        double value2 = dividend.doubleValue();

        return value1 % value2 == 0;
    }

    @JtwigFunction(name = "even")
    public boolean even(@Parameter int number) {
        return number % 2 == 0;
    }

    @JtwigFunction(name = "odd")
    public boolean odd(@Parameter int number) {
        return number % 2 == 1;
    }

    @JtwigFunction(name = "null")
    public boolean isNull(@Parameter Object input) {
        return input == null || input instanceof Undefined;
    }

    @JtwigFunction(name = "iterable")
    public boolean iterable(@Parameter Object input) {
        return input instanceof Iterable
                || input.getClass().isArray()
                || input instanceof Map;
    }

    @JtwigFunction(name = "empty")
    public boolean isEmpty(@Parameter Object input) {
        return AnyOf.<Object>anyOf(
                nullValue(Object.class),
                emptyCollection(),
                emptyMap(),
                notHasNext(),
                zeroValue()
        ).matches(input);
    }

    private Matcher<Object> emptyMap() {
        return new CastMatcher<>(Map.class, new FeatureMatcher<Map, Collection>(emptyCollection(), "empty list", "empty list") {
            @Override
            protected Collection featureValueOf(Map actual) {
                return actual.keySet();
            }
        });
    }

    private Matcher<Object> zeroValue() {
        return new CastMatcher<>(Integer.class, equalTo(0));
    }

    private Matcher<Object> emptyCollection() {
        return new CastMatcher<>(Collection.class, (Matcher) empty());
    }

    private Matcher<Object> notHasNext() {
        return new CastMatcher<>(Iterable.class, new FeatureMatcher<Iterable, Boolean>(equalTo(false), "has next", "has next") {
            @Override
            protected Boolean featureValueOf(Iterable actual) {
                return actual.iterator().hasNext();
            }
        });
    }
}
