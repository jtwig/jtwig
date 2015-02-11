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

package org.jtwig.acceptance.functions;

import org.jtwig.AbstractJtwigTest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.jtwig.util.SyntacticSugar.then;

public class ListFunctionsTest extends AbstractJtwigTest {
    @Test
    public void batch() throws Exception {
        withResource("{{ batch([1,2,3,4], 3) }}");
        then(theResult(), is(equalTo("[[1, 2, 3], [4]]")));
    }

    @Test
    public void batchWithPadding() throws Exception {
        withResource("{{ batch([1,2,3,4], 3, 1) }}");
        then(theResult(), is(equalTo("[[1, 2, 3], [4, 1, 1]]")));
    }

    @Test
    public void concatenate() throws Exception {
        withResource("{{ concatenate(1, 2, 3) }}");
        then(theResult(), is(equalTo("123")));
    }

    @Test
    public void join() throws Exception {
        withResource("{{ join([1, 2, 3]) }}");
        then(theResult(), is(equalTo("123")));
    }

    @Test
    public void joinWithCustomSeparator() throws Exception {
        withResource("{{ join([1, 2, 3], ' - ') }}");
        then(theResult(), is(equalTo("1 - 2 - 3")));
    }

    @Test
    public void merge() throws Exception {
        withResource("{{ merge([1, 2, 3], [4]) }}");
        then(theResult(), is(equalTo("[1, 2, 3, 4]")));
    }
    @Test
    public void mergeMultipleArgs() throws Exception {
        withResource("{{ merge([1, 2, 3], [4], [5, 6]) }}");
        then(theResult(), is(equalTo("[1, 2, 3, 4, 5, 6]")));
    }
    @Test
    public void slice() throws Exception {
        withResource("{{ slice('abc', 1, 2) }}");
        then(theResult(), is(equalTo("bc")));
    }
    @Test
    public void sort() throws Exception {
        withResource("{{ sort([2, 1, 3]) }}");
        then(theResult(), is(equalTo("[1, 2, 3]")));
    }
}
