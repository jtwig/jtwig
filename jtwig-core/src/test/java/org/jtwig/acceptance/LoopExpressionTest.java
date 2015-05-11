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

package org.jtwig.acceptance;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

import java.util.HashMap;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class LoopExpressionTest {
    @Test
    public void simpleLoop() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("list", asList("a", "b", "c"));

        String result = JtwigTemplate
            .inlineTemplate("{% for item in list %}{{ item }}{% endfor %}")
            .render(model);

        assertThat(result, is(equalTo("abc")));
    }
    @Test
    public void simpleLoopLength() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("list", asList("a", "b", "c"));

        String result = JtwigTemplate
            .inlineTemplate("{% for item in list %}{{ loop.length }}{% endfor %}")
            .render(model);

        assertThat(result, is(equalTo("333")));
    }

    @Test
    public void simpleMapLoopWithList() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("list", asList("a"));

        String result = JtwigTemplate
            .inlineTemplate("{% for key, value in list %}{{ key }} - {{ value }}{% endfor %}")
            .render(model);

        assertThat(result, is(equalTo("0 - a")));
    }

    @Test
    public void emptyMap() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("list", new HashMap<>());

        String result = JtwigTemplate
            .inlineTemplate("{% for key, value in list %}{{ key }} - {{ value }}{% else %}Test{% endfor %}")
            .render(model);

        assertThat(result, is(equalTo("Test")));
    }
}
