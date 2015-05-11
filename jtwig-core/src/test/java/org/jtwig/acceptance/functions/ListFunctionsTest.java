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

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListFunctionsTest {
    @Test
    public void batch() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ batch([1,2,3,4], 3) }}")
            .render(model);

        assertThat(result, is(equalTo("[[1, 2, 3], [4]]")));
    }

    @Test
    public void batchWithPadding() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ batch([1,2,3,4], 3, 1) }}")
            .render(model);

        assertThat(result, is(equalTo("[[1, 2, 3], [4, 1, 1]]")));
    }

    @Test
    public void concatenate() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ concatenate(1, 2, 3) }}")
            .render(model);

        assertThat(result, is(equalTo("123")));
    }

    @Test
    public void join() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ join([1, 2, 3]) }}")
            .render(model);

        assertThat(result, is(equalTo("123")));
    }

    @Test
    public void joinWithCustomSeparator() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ join([1, 2, 3], ' - ') }}")
            .render(model);

        assertThat(result, is(equalTo("1 - 2 - 3")));
    }

    @Test
    public void merge() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ merge([1, 2, 3], [4]) }}")
            .render(model);

        assertThat(result, is(equalTo("[1, 2, 3, 4]")));
    }
    @Test
    public void mergeMultipleArgs() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ merge([1, 2, 3], [4], [5, 6]) }}")
            .render(model);

        assertThat(result, is(equalTo("[1, 2, 3, 4, 5, 6]")));
    }
    @Test
    public void slice() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ slice('abc', 1, 2) }}")
            .render(model);

        assertThat(result, is(equalTo("bc")));
    }

    @Test
    public void sort() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ sort([2, 1, 3]) }}")
            .render(model);

        assertThat(result, is(equalTo("[1, 2, 3]")));
    }
}
