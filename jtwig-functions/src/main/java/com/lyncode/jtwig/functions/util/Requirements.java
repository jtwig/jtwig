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

package com.lyncode.jtwig.functions.util;

import com.lyncode.jtwig.functions.exceptions.FunctionException;
import org.hamcrest.*;

public class Requirements {
    public static Requirements requires(Object... arguments) {
        return new Requirements(arguments);
    }

    private Object[] arguments;

    public Requirements (Object... arguments) {
        this.arguments = arguments;
    }



    public Requirements withNumberOfArguments(Matcher<Integer> predicate) throws FunctionException {
        BaseDescription description = new StringDescription();
        predicate.describeTo(description);
        if (!predicate.matches(arguments.length))
            throw new FunctionException("Invalid number of arguments, it should be "+description.toString());
        return this;
    }

    public Requirements withArgument (int number, Matcher<Object> predicate) throws FunctionException {
        BaseDescription description = new StringDescription();
        predicate.describeTo(description);
        if (!predicate.matches(arguments[number]))
            throw new FunctionException("Invalid argument "+number+" ("+arguments[number]+"). It should "+description.toString());
        return this;
    }


    public static Matcher<Object> isArray () {
        return new BaseMatcher<Object>() {
            @Override
            public boolean matches(Object item) {
                return item.getClass().isArray();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is array");
            }
        };
    }

    public static Matcher<Integer> between (final int start, final int end) {
        return new TypeSafeMatcher<Integer>() {
            @Override
            protected boolean matchesSafely(Integer item) {
                return item >= start && item <= end;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("between ").appendValue(start).appendText(" and ").appendValue(end);
            }
        };
    }
}
