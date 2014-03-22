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

package com.lyncode.jtwig.functions.internal.bool;

import com.lyncode.jtwig.functions.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.JtwigFunctionDeclaration;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.Map;

import static com.lyncode.jtwig.functions.util.Requirements.requires;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsEmptyCollection.empty;

@JtwigFunctionDeclaration(name = "empty")
public class Empty implements JtwigFunction {
    @Override
    public Object execute(Object... arguments) throws FunctionException {
        requires(arguments)
                .withNumberOfArguments(equalTo(1));

        return anyOf(
                arguments[0],
                nullValue(Object.class),
                emptyList(),
                emptyMap(),
                notHasNext(),
                zeroValue()
        );
    }

    private boolean anyOf (Object value, Matcher... matchers) {
        for (Matcher matcher : matchers)
            if (matcher.matches(value))
                return true;
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
