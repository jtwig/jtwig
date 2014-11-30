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

package org.jtwig.util;

import org.hamcrest.Matcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;

public class SyntacticSugar {
    public static <T> T when (T value) {
        return value;
    }
    public static <T> T given (T value) {
        return value;
    }
    public static <T> T after (T value) {
        return value;
    }

    public static <T> void then (T actual, Matcher<? super T>... matchers) {
        assertThat(actual, allOf(matchers));
    }
}
