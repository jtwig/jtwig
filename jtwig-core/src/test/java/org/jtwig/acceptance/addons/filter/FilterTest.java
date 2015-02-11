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

package org.jtwig.acceptance.addons.filter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import org.jtwig.acceptance.addons.AbstractAddonTest;
import org.jtwig.exception.ParseException;
import org.junit.Test;

public class FilterTest extends AbstractAddonTest {
    @Test(expected = ParseException.class)
    public void emptyFilter() throws Exception {
        withResource("{% filter %}a{% endfilter %}");
        assertThat(theResult(), equalTo("a"));
    }

    @Test
    public void singleFilter() throws Exception {
        withResource("{% filter upper %}a{% endfilter %}");
        assertThat(theResult(), equalTo("A"));
    }

    @Test
    public void singleFilterWithParam() throws Exception {
        withResource("{% filter format('apple') %}a is for %s{% endfilter %}");
        assertThat(theResult(), equalTo("a is for apple"));
    }

    @Test
    public void singleFilterWithParams() throws Exception {
        withResource("{% filter format('a', 'apple') %}%s is for %s{% endfilter %}");
        assertThat(theResult(), equalTo("a is for apple"));
    }

    @Test
    public void doubleFilter() throws Exception {
        withResource("{% filter upper|escape %}<a>{% endfilter %}");
        assertThat(theResult(), equalTo("&lt;A&gt;"));
    }

    @Test
    public void doubleFilterWithParamFirst() throws Exception {
        withResource("{% filter format('apple')|upper %}a is for %s{% endfilter %}");
        assertThat(theResult(), equalTo("A IS FOR APPLE"));
    }

    @Test
    public void doubleFilterWithParamSecond() throws Exception {
        withResource("{% filter upper|format('apple') %}a is for %s{% endfilter %}");
        assertThat(theResult(), equalTo("A IS FOR APPLE"));
    }

    @Test
    public void doubleFilterWithParamsFirst() throws Exception {
        withResource("{% filter format('a', 'apple')|upper %}%s is for %s{% endfilter %}");
        assertThat(theResult(), equalTo("A IS FOR APPLE"));
    }

    @Test
    public void doubleFilterWithParamsSecond() throws Exception {
        withResource("{% filter upper|format('a', 'apple') %}%s is for %s{% endfilter %}");
        assertThat(theResult(), equalTo("A IS FOR APPLE"));
    }

    @Test
    public void tripleFilter() throws Exception {
        withResource("{% filter upper|escape|lower %}<a>{% endfilter %}");
        assertThat(theResult(), equalTo("&lt;a&gt;"));
    }

    @Test
    public void tripleFilterWithParamFirst() throws Exception {
        withResource("{% filter format('apple')|upper|lower %}a is for %s{% endfilter %}");
        assertThat(theResult(), equalTo("a is for apple"));
    }

    @Test
    public void tripleFilterWithParamSecond() throws Exception {
        withResource("{% filter upper|format('apple')|lower %}a is for %s{% endfilter %}");
        assertThat(theResult(), equalTo("a is for apple"));
    }

    @Test
    public void tripleFilterWithParamThird() throws Exception {
        withResource("{% filter upper|escape|format('apple') %}<%s>{% endfilter %}");
        assertThat(theResult(), equalTo("&lt;APPLE&gt;"));
    }

    @Test
    public void tripleFilterWithParamsFirst() throws Exception {
        withResource("{% filter format('a', 'apple')|upper|lower %}%s is for %s{% endfilter %}");
        assertThat(theResult(), equalTo("a is for apple"));
    }

    @Test
    public void tripleFilterWithParamsSecond() throws Exception {
        withResource("{% filter upper|format('a', 'apple')|lower %}%s is for %s{% endfilter %}");
        assertThat(theResult(), equalTo("a is for apple"));
    }

    @Test
    public void tripleFilterWithParamsThird() throws Exception {
        withResource("{% filter upper|lower|format('a', 'apple') %}%s is for %s{% endfilter %}");
        assertThat(theResult(), equalTo("a is for apple"));
    }
}