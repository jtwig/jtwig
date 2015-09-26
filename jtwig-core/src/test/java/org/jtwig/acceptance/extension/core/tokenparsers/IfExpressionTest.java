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

import java.util.Arrays;
import java.util.Collections;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

public class IfExpressionTest {
    @Test
    public void ifWithEmptyListShouldBeTheSameAsFalse () throws Exception {
        JtwigModelMap model = new JtwigModelMap().withModelAttribute("list", Collections.EMPTY_LIST);

        String result = JtwigTemplate
            .inlineTemplate("{% if (list) %}not empty{% else %}empty{% endif %}")
            .render(model);

        assertThat(result, is("empty"));
    }

    @Test
    public void ifInOperator () throws Exception {
        String result = JtwigTemplate
            .inlineTemplate("{% if (1 in [1, 2]) %}ok{% else %}ko{% endif %}")
            .render(new JtwigModelMap());

        assertThat(result, is("ok"));
    }

    @Test
    public void ifNotInOperator () throws Exception {
        String result = JtwigTemplate
            .inlineTemplate("{% if (1 not in [3, 2]) %}ok{% else %}ko{% endif %}")
            .render(new JtwigModelMap());

        assertThat(result, is("ok"));
    }

    @Test
    public void ifWithNonEmptyListShouldBeTheSameAsTrue () throws Exception {
        String result = JtwigTemplate
            .inlineTemplate("{% if (list) %}not empty{% else %}empty{% endif %}")
            .render(new JtwigModelMap().withModelAttribute("list", Arrays.asList("a")));

        assertThat(result, is("not empty"));
    }

    @Test
    public void IfWithContentInside () throws Exception {
        String result = JtwigTemplate
            .inlineTemplate("{% if (items) %}" +
                            "Items: "+
                            "{% for item in items %}" +
                            "{{ item }}" +
                            "{% endfor %}" +
                            "{% endif %}")
            .render(new JtwigModelMap().withModelAttribute("items", Arrays.asList("a")));

        assertThat(result, is("Items: a"));
    }
}
