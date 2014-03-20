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

package com.lyncode.jtwig.functions.internal.list;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class BatchTest {
    private Batch underTest = new Batch();

    @Test
    public void testExecute() throws Exception {
        List<List<String>> result = (List<List<String>>) underTest.execute(new String[]{"a", "b", "c", "d", "e", "f"}, 3);
        assertThat(result.get(0).size(), is(3));
        assertThat(result.get(0), hasItem("a"));
        assertThat(result.get(0), hasItem("b"));
        assertThat(result.get(0), hasItem("c"));
        assertThat(result.get(1).size(), is(3));
        assertThat(result.get(1), hasItem("d"));
        assertThat(result.get(1), hasItem("e"));
        assertThat(result.get(1), hasItem("f"));
    }

    @Test
    public void testExecuteWithNotMultipleSize() throws Exception {
        List<List<String>> result = (List<List<String>>) underTest.execute(new String[]{"a", "b", "c", "d", "e", "f", "g"}, 3);
        assertThat(result.get(0).size(), is(3));
        assertThat(result.get(0), hasItem("a"));
        assertThat(result.get(0), hasItem("b"));
        assertThat(result.get(0), hasItem("c"));
        assertThat(result.get(1).size(), is(3));
        assertThat(result.get(1), hasItem("d"));
        assertThat(result.get(1), hasItem("e"));
        assertThat(result.get(1), hasItem("f"));
        assertThat(result.get(2).size(), is(1));
    }

    @Test
    public void testExecuteWithNotMultipleSizeWithDefault() throws Exception {
        List<List<String>> result = (List<List<String>>) underTest.execute(new String[]{"a", "b", "c", "d", "e", "f", "g"}, 3, "h");
        assertThat(result.get(0).size(), is(3));
        assertThat(result.get(0), hasItem("a"));
        assertThat(result.get(0), hasItem("b"));
        assertThat(result.get(0), hasItem("c"));
        assertThat(result.get(1).size(), is(3));
        assertThat(result.get(1), hasItem("d"));
        assertThat(result.get(1), hasItem("e"));
        assertThat(result.get(1), hasItem("f"));
        assertThat(result.get(2).size(), is(3));
    }
}
