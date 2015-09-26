/**
 *
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

import java.io.ByteArrayOutputStream;
import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.jtwig.Environment;
import org.jtwig.render.stream.RenderStream;
import org.mockito.Mockito;

public class ConcurrentTest {
    @Test
    public void concurrentWithStaticContent() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% concurrent %}a{% endconcurrent %}b")
            .render(model);

        assertThat(result, is(equalTo("ab")));
    }

    @Test
    public void concurrentWithConditionalContent() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% concurrent %}{% if true %}a{% endif %}{% endconcurrent %}b")
            .render(model);

        assertThat(result, is(equalTo("ab")));
    }

    @Test
    public void doubleConcurrentWithStaticContent() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% concurrent %}a{% endconcurrent %}"
                            +"{% concurrent %}b{% endconcurrent %}"
                            +"c")
            .render(model);

        assertThat(result, is(equalTo("abc")));
    }

    @Test
    public void concurrentWithDynamicContent() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("list", asList("a", "b", "c", "d"));

        String result = JtwigTemplate
            .inlineTemplate("{% concurrent %}{% for item in list %}{{ item }}{% endfor %}{% endconcurrent %}")
            .render(model);

        assertThat(result, is(equalTo("abcd")));
    }

    @Test
    public void test_concurrent_1() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("list", asList("a", "b", "c", "d", "e"));

        String result = JtwigTemplate
            .inlineTemplate("{% concurrent %}{% for item in list %}" +
                            "{% if loop.first %}{% concurrent %}First {% endconcurrent %}{% elseif loop.last %}{% concurrent %}Last{% endconcurrent %}{% else %}I: {{ loop.index0 }} R: {{ loop.revindex0 }} {% endif %}" +
                            "{% endfor %}{% endconcurrent %}")
            .render(model);

        assertThat(result, is(equalTo("First I: 1 R: 3 I: 2 R: 2 I: 3 R: 1 Last")));
    }

    @Test
    public void test_concurrent_2() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% concurrent %}1{% endconcurrent %}{% concurrent %}{% concurrent %}{% concurrent %}2{% endconcurrent %}{% endconcurrent %}{% endconcurrent %}{% concurrent %}3{% endconcurrent %}{% concurrent %}{% concurrent %}{% concurrent %}{% concurrent %}{% concurrent %}{% concurrent %}4{% endconcurrent %}{% endconcurrent %}{% endconcurrent %}{% endconcurrent %}{% endconcurrent %}{% endconcurrent %}5{% concurrent %}6{% endconcurrent %}{% concurrent %}7{% endconcurrent %}")
            .render(model);

        assertThat(result, is(equalTo("1234567")));
    }
    
}