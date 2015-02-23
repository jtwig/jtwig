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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

public class ForExpressionTest {
    @Test
    public void emptyListShouldOutputNothing () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("list", Collections.EMPTY_LIST);

        String result = JtwigTemplate
            .inlineTemplate("{% for item in list %}Item {{ item }}{% endfor %}")
            .render(model);

        assertThat(result, is(""));
    }
    @Test
    public void nonEmptyListShouldOutputSomething () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("list", Arrays.asList("a"));

        String result = JtwigTemplate
            .inlineTemplate("{% for item in list %}Item {{ item }}{% endfor %}")
            .render(model);

        assertThat(result, is("Item a"));
    }

    @Test
    public void forLoopMustExposeTheLoopVariable () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("list", Arrays.asList("a","b","c","d","e"));

        String result = JtwigTemplate
            .inlineTemplate("{% for item in list %}" +
                            "{% if loop.first %}First {% elseif loop.last %}Last{% else %}I: {{ loop.index0 }} R: {{ loop.revindex0 }} {% endif %}" +
                            "{% endfor %}")
            .render(model);

        assertThat(result, is("First I: 1 R: 3 I: 2 R: 2 I: 3 R: 1 Last"));
    }

    @Test
    public void ensureProperLoopVariableIndexing () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("list", Arrays.asList("a","b","c","d","e"));

        String result = JtwigTemplate
            .inlineTemplate("{% for item in list %}" +
                            "{{ loop.index0 }}{{ loop.index }}{{ loop.revindex0 }}{{ loop.revindex }} " +
                            "{% endfor %}")
            .render(model);

        assertThat(result, is("0145 1234 2323 3412 4501 "));
    }

    @Test
    public void shouldNotOutputNothingIfListIsNull () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("list", null);

        String result = JtwigTemplate
            .inlineTemplate("{% for item in list %}a{% endfor %}")
            .render(model);

        assertThat(result, is(""));
    }


    @Test
    public void iterateOverMap () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        LinkedHashMap<String, String> value = new  LinkedHashMap<String, String>();
        value.put("one", "1");
        value.put("two", "2");
        value.put("three", "3");
        model.withModelAttribute("map", value);

        String result = JtwigTemplate
            .inlineTemplate("{% for key, value in map %}{{ key }} = {{ value }}|{% endfor %}")
            .render(model);

        assertThat(result, is("one = 1|two = 2|three = 3|"));
    }
    
    @Test
    public void avoidElseOnPopulatedList () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("list", Arrays.asList("a","b"));

        String result = JtwigTemplate
            .inlineTemplate("{% for value in list %}{{ value }}{% else %}nothing{% endfor %}")
            .render(model);

        assertThat(result, is("ab"));
    }
    
    @Test
    public void outputElseOnEmptyList () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("list", Collections.EMPTY_LIST);

        String result = JtwigTemplate
            .inlineTemplate("{% for value in list %}{{ value }}{% else %}nothing{% endfor %}")
            .render(model);

        assertThat(result, is("nothing"));
    }
    
    @Test
    public void outputElseOnUndefined () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% for value in var %}{{ value }}{% else %}nothing{% endfor %}")
            .render(model);

        assertThat(result, is("nothing"));
    }
    
    @Test
    public void iterateOnSelection () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("obj", new Obj())
            .withModelAttribute("name", "Test");

        String result = JtwigTemplate
            .inlineTemplate("{% for value in obj.getList(name) %}{{ value }}{% endfor %}")
            .render(model);

        assertThat(result, is("ab"));
    }
    
    @Test
    public void iterateOnSequence () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("start", 'b')
            .withModelAttribute("end", 'l');

        String result = JtwigTemplate
            .inlineTemplate("{% for value in start..end %}{{ value }}{% endfor %}")
            .render(model);

        assertThat(result, is("bcdefghijkl"));
    }
    
    @Test
    public void iterateOnEnumArray () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("values", TestEnum.values());

        String result = JtwigTemplate
            .inlineTemplate("{% for v in values %}{{ v }}{% endfor %}")
            .render(model);

        assertThat(result, is("ABC"));
    }
    
    public static class Obj {
        private final List<String> list = new ArrayList<String>(){{
            add("a");
            add("b");
        }};

        public List<String> getList(String name) {
            return list;
        }
    }
    enum TestEnum {
        A, B, C
    }
}
