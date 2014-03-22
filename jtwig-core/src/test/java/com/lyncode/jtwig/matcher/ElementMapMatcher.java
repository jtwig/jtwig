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

package com.lyncode.jtwig.matcher;

import com.lyncode.jtwig.tree.expressions.ValueMap;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsMapContaining;

import static org.hamcrest.CoreMatchers.equalTo;

public class ElementMapMatcher<K, V> extends BaseMatcher<ValueMap> {
    public static <K, V> ElementMapMatcher<K, V> mapsElement (Matcher<? super K> keyMatcher, Matcher<? super V> valueMatcher) {
        return new ElementMapMatcher<K, V>(keyMatcher, valueMatcher);
    }
    public static <K, V> ElementMapMatcher<K, V> mapsElement (K key, Matcher<? super V> valueMatcher) {
        return new ElementMapMatcher<K, V>(equalTo(key), valueMatcher);
    }
    public static <K, V> ElementMapMatcher<K, V> mapsElement (K key, V value) {
        return new ElementMapMatcher<K, V>(equalTo(key), Matchers.equalTo(value));
    }

    public static <T> BaseMatcher<T> emptyElementMap () {
        return new BaseMatcher<T>() {
            @Override
            public boolean matches(Object item) {
                return ((ValueMap) item).getMap().isEmpty();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("empty map");
            }
        };
    }

    private final IsMapContaining<K, V> mapContaining;

    public ElementMapMatcher(Matcher<? super K> keyMatcher, Matcher<? super V> valueMatcher) {
        mapContaining = new IsMapContaining<K, V>(keyMatcher, valueMatcher);
    }

    @Override
    public boolean matches(Object o) {
        return mapContaining.matches(((ValueMap) o).getMap());
    }

    @Override
    public void describeTo(Description description) {
        mapContaining.describeTo(description);
    }
}
