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

package com.lyncode.jtwig.acceptance.addons.filter;

import com.lyncode.jtwig.acceptance.addons.AbstractAddonTest;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import org.junit.Test;

import static com.lyncode.jtwig.util.SyntacticSugar.after;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class FilterTest extends AbstractAddonTest {
    @Test(expected = ParseException.class)
    public void emptyFilter() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(template("{% filter %}a{% endfilter %}")));
        assertThat(theRenderedTemplate(), equalTo("a"));
    }

    @Test
    public void singleFilter() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(template("{% filter upper %}a{% endfilter %}")));
        assertThat(theRenderedTemplate(), equalTo("A"));
    }

    @Test
    public void singleFilterWithParam() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(template("{% filter format('apple') %}a is for %s{% endfilter %}")));
        assertThat(theRenderedTemplate(), equalTo("a is for apple"));
    }

    @Test
    public void singleFilterWithParams() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(template("{% filter format('a', 'apple') %}%s is for %s{% endfilter %}")));
        assertThat(theRenderedTemplate(), equalTo("a is for apple"));
    }

    @Test
    public void doubleFilter() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(template("{% filter upper|escape %}<a>{% endfilter %}")));
        assertThat(theRenderedTemplate(), equalTo("&lt;A&gt;"));
    }

    @Test
    public void doubleFilterWithParamFirst() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(template("{% filter format('apple')|upper %}a is for %s{% endfilter %}")));
        assertThat(theRenderedTemplate(), equalTo("A IS FOR APPLE"));
    }

    @Test
    public void doubleFilterWithParamSecond() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(template("{% filter upper|format('apple') %}a is for %s{% endfilter %}")));
        assertThat(theRenderedTemplate(), equalTo("A IS FOR APPLE"));
    }

    @Test
    public void doubleFilterWithParamsFirst() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(template("{% filter format('a', 'apple')|upper %}%s is for %s{% endfilter %}")));
        assertThat(theRenderedTemplate(), equalTo("A IS FOR APPLE"));
    }

    @Test
    public void doubleFilterWithParamsSecond() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(template("{% filter upper|format('a', 'apple') %}%s is for %s{% endfilter %}")));
        assertThat(theRenderedTemplate(), equalTo("A IS FOR APPLE"));
    }

    @Test
    public void tripleFilter() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(template("{% filter upper|escape|lower %}<a>{% endfilter %}")));
        assertThat(theRenderedTemplate(), equalTo("&lt;a&gt;"));
    }

    @Test
    public void tripleFilterWithParamFirst() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(template("{% filter format('apple')|upper|lower %}a is for %s{% endfilter %}")));
        assertThat(theRenderedTemplate(), equalTo("a is for apple"));
    }

    @Test
    public void tripleFilterWithParamSecond() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(template("{% filter upper|format('apple')|lower %}a is for %s{% endfilter %}")));
        assertThat(theRenderedTemplate(), equalTo("a is for apple"));
    }

    @Test
    public void tripleFilterWithParamThird() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(template("{% filter upper|escape|format('apple') %}<%s>{% endfilter %}")));
        assertThat(theRenderedTemplate(), equalTo("&lt;APPLE&gt;"));
    }

    @Test
    public void tripleFilterWithParamsFirst() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(template("{% filter format('a', 'apple')|upper|lower %}%s is for %s{% endfilter %}")));
        assertThat(theRenderedTemplate(), equalTo("a is for apple"));
    }

    @Test
    public void tripleFilterWithParamsSecond() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(template("{% filter upper|format('a', 'apple')|lower %}%s is for %s{% endfilter %}")));
        assertThat(theRenderedTemplate(), equalTo("a is for apple"));
    }

    @Test
    public void tripleFilterWithParamsThird() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(template("{% filter upper|lower|format('a', 'apple') %}%s is for %s{% endfilter %}")));
        assertThat(theRenderedTemplate(), equalTo("a is for apple"));
    }
}
