/**
 * Copyright 2012 Lyncode
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

package com.lyncode.jtwig.addons.spaceless;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.JtwigTemplate;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConcurrentTest extends AbstractAddonTest {

    @Test
    public void test_concurrent_1() throws Exception {
        JtwigTemplate template = new JtwigTemplate("{% concurrent %}{% for item in list %}" +
                                                           "{% if loop.first %}{% concurrent %}First {% endconcurrent %}{% elseif loop.last %}{% concurrent %}Last{% endconcurrent %}{% else %}I: {{ loop.index }} R: {{ loop.revindex }} {% endif %}" +
                                                           "{% endfor %}{% endconcurrent %}");
        JtwigContext context = new JtwigContext();
        ArrayList<String> value = new ArrayList<String>();
        value.add("a");
        value.add("b");
        value.add("c");
        value.add("d");
        value.add("e");
        context.withModelAttribute("list", value);
        assertThat(template.output(context), is("First I: 1 R: 3 I: 2 R: 2 I: 3 R: 1 Last"));
    }

    @Test
    public void test_concurrent_2() throws Exception {
        JtwigTemplate template2 = new JtwigTemplate(
                "{% concurrent %}1{% endconcurrent %}{% concurrent %}{% concurrent %}{% concurrent %}2{% endconcurrent %}{% endconcurrent %}{% endconcurrent %}{% concurrent %}3{% endconcurrent %}{% concurrent %}{% concurrent %}{% concurrent %}{% concurrent %}{% concurrent %}{% concurrent %}4{% endconcurrent %}{% endconcurrent %}{% endconcurrent %}{% endconcurrent %}{% endconcurrent %}{% endconcurrent %}5{% concurrent %}6{% endconcurrent %}{% concurrent %}7{% endconcurrent %}");
        JtwigContext context = new JtwigContext();
        assertThat(template2.output(context), is("1234567"));
    }
}
