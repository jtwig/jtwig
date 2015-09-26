/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.extension.core.tests;

import java.util.Collection;
import java.util.Map;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import org.hamcrest.core.AnyOf;
import org.jtwig.extension.api.test.AbstractTest;
import org.jtwig.functions.util.CastMatcher;

public class EmptyTest extends AbstractTest {

    @Override
    public boolean evaluate(Object left, Object... args) {
        return AnyOf.<Object>anyOf(
                nullValue(Object.class),
                emptyCollection(),
                emptyMap(),
                notHasNext(),
                zeroValue()
        ).matches(left);
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