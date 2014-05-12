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

package com.lyncode.jtwig.util.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsInstanceOf.instanceOf;

public class ExceptionMatcher extends BaseMatcher<Exception> {
    public static ExceptionMatcher exception () {
        return new ExceptionMatcher();
    }

    private List<Matcher<? super Exception>> matchers = new ArrayList<>();

    @Override
    public boolean matches(Object item) {
        return getMatcher().matches(item);
    }

    private Matcher<Exception> getMatcher() {
        return AllOf.allOf(matchers);
    }

    @Override
    public void describeTo(Description description) {
        description.appendDescriptionOf(getMatcher());
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        getMatcher().describeMismatch(item, description);
    }

    public ExceptionMatcher withInnerException(final ExceptionMatcher matcher) {
        matchers.add(new FeatureMatcher<Exception, Exception>(matcher, "inner exception", "") {
            @Override
            protected Exception featureValueOf(Exception actual) {
                return (Exception) actual.getCause();
            }
        });
        return this;
    }

    public ExceptionMatcher message(final Matcher<String> stringMatcher) {
        matchers.add(new FeatureMatcher<Exception, String>(stringMatcher, "message", "") {
            @Override
            protected String featureValueOf(Exception actual) {
                return actual.getMessage();
            }
        });
        return this;
    }

    public ExceptionMatcher ofType(Class<?> type) {
        matchers.add(instanceOf(type));
        return this;
    }
}
