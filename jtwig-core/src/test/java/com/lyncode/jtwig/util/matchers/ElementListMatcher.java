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

import com.lyncode.jtwig.tree.helper.ElementList;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsCollectionContaining;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.empty;

public class ElementListMatcher<T> extends BaseMatcher<ElementList> {
    public static <T> ElementListMatcher<T> hasElement (Matcher<? super T> elementMatcher) {
        return new ElementListMatcher<T>(elementMatcher);
    }
    public static <T> ElementListMatcher<T> hasElement (T elementMatcher) {
        return new ElementListMatcher<T>(equalTo(elementMatcher));
    }
    public static <T> BaseMatcher<T> emptyElementList () {
        return new BaseMatcher<T>() {
            @Override
            public boolean matches(Object item) {
                return empty().matches(((ElementList) item).getList());
            }

            @Override
            public void describeTo(Description description) {
                empty().describeTo(description);
            }
        };
    }

    private final IsCollectionContaining<T> listContains;

    public ElementListMatcher(Matcher<? super T> matcher) {
        listContains = new IsCollectionContaining<T>(matcher);
    }

    @Override
    public boolean matches(Object o) {
        return listContains.matches(((ElementList) o).getList());
    }

    @Override
    public void describeTo(Description description) {
        listContains.describeTo(description);
    }
}
