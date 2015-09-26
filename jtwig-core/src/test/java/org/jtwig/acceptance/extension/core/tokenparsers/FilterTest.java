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

package org.jtwig.acceptance.extension.core.tokenparsers;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.jtwig.exception.ParseException;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class FilterTest {
    @Test(expected = ParseException.class)
    public void emptyFilter() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% filter %}a{% endfilter %}")
            .render(model);

        assertThat(result, equalTo("a"));
    }

    @Test
    public void singleFilter() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% filter upper %}a{% endfilter %}")
            .render(model);

        assertThat(result, equalTo("A"));
    }

    @Test
    public void singleFilterWithParam() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% filter format('apple') %}a is for %s{% endfilter %}")
            .render(model);

        assertThat(result, equalTo("a is for apple"));
    }

    @Test
    public void singleFilterWithParams() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% filter format('a', 'apple') %}%s is for %s{% endfilter %}")
            .render(model);

        assertThat(result, equalTo("a is for apple"));
    }

    @Test
    public void doubleFilter() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% filter upper|escape %}<a>{% endfilter %}")
            .render(model);

        assertThat(result, equalTo("&lt;A&gt;"));
    }

    @Test
    public void doubleFilterWithParamFirst() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% filter format('apple')|upper %}a is for %s{% endfilter %}")
            .render(model);

        assertThat(result, equalTo("A IS FOR APPLE"));
    }

    @Test
    public void doubleFilterWithParamSecond() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% filter upper|format('apple') %}a is for %s{% endfilter %}")
            .render(model);

        assertThat(result, equalTo("A IS FOR APPLE"));
    }

    @Test
    public void doubleFilterWithParamsFirst() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% filter format('a', 'apple')|upper %}%s is for %s{% endfilter %}")
            .render(model);

        assertThat(result, equalTo("A IS FOR APPLE"));
    }

    @Test
    public void doubleFilterWithParamsSecond() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% filter upper|format('a', 'apple') %}%s is for %s{% endfilter %}")
            .render(model);

        assertThat(result, equalTo("A IS FOR APPLE"));
    }

    @Test
    public void tripleFilter() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% filter upper|escape|lower %}<a>{% endfilter %}")
            .render(model);

        assertThat(result, equalTo("&lt;a&gt;"));
    }

    @Test
    public void tripleFilterWithParamFirst() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% filter format('apple')|upper|lower %}a is for %s{% endfilter %}")
            .render(model);

        assertThat(result, equalTo("a is for apple"));
    }

    @Test
    public void tripleFilterWithParamSecond() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% filter upper|format('apple')|lower %}a is for %s{% endfilter %}")
            .render(model);

        assertThat(result, equalTo("a is for apple"));
    }

    @Test
    public void tripleFilterWithParamThird() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% filter upper|escape|format('apple') %}<%s>{% endfilter %}")
            .render(model);

        assertThat(result, equalTo("&lt;APPLE&gt;"));
    }

    @Test
    public void tripleFilterWithParamsFirst() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% filter format('a', 'apple')|upper|lower %}%s is for %s{% endfilter %}")
            .render(model);

        assertThat(result, equalTo("a is for apple"));
    }

    @Test
    public void tripleFilterWithParamsSecond() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% filter upper|format('a', 'apple')|lower %}%s is for %s{% endfilter %}")
            .render(model);

        assertThat(result, equalTo("a is for apple"));
    }

    @Test
    public void tripleFilterWithParamsThird() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% filter upper|lower|format('a', 'apple') %}%s is for %s{% endfilter %}")
            .render(model);

        assertThat(result, equalTo("a is for apple"));
    }
}