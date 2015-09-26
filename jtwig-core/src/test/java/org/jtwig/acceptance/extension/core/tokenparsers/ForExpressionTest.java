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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ForExpressionTest {
    @Test
    public void generalTests() throws Exception {
        assertEquals("", JtwigTemplate.inlineTemplate("{% for item in [] %}{{ item }}{% endfor %}").render());
        assertEquals("abc", JtwigTemplate.inlineTemplate("{% for item in ['a','b','c'] %}{{ item }}{% endfor %}").render());
        assertEquals("333", JtwigTemplate.inlineTemplate("{% for item in ['a','b','c'] %}{{ loop.length }}{% endfor %}").render());
        assertEquals("0a", JtwigTemplate.inlineTemplate("{% for key, value in ['a'] %}{{ key }}{{ value }}{% endfor %}").render());
        assertEquals("alt", JtwigTemplate.inlineTemplate("{% for key, value in [] %}{{ key }}{{ value }}{% else %}alt{% endfor %}").render());
        assertEquals("one = 1|two = 2|three = 3|", JtwigTemplate.inlineTemplate("{% for key, value in {'one': '1', 'two': '2', 'three': '3'} %}{{ key }} = {{ value }}|{% endfor %}").render());
        assertEquals("nothing", JtwigTemplate.inlineTemplate("{% for value in missing %}{{ value }}{% else %}nothing{% endfor %}").render());
    }

    @Test
    public void forLoopMustExposeTheLoopVariable () throws Exception {
        String result = JtwigTemplate.inlineTemplate("{% for item in list %}" +
                "{% if loop.first %}First {% elseif loop.last %}Last{% else %}I: {{ loop.index0 }} R: {{ loop.revindex0 }} {% endif %}" +
                "{% endfor %}").render(new JtwigModelMap(Collections.singletonMap("list", (Object)Arrays.asList("a","b","c","d","e"))));
        assertThat(result, is("First I: 1 R: 3 I: 2 R: 2 I: 3 R: 1 Last"));
    }

    @Test
    public void ensureProperLoopVariableIndexing () throws Exception {
        String result = JtwigTemplate.inlineTemplate("{% for item in list %}" +
                "{{ loop.index0 }}{{ loop.index }}{{ loop.revindex0 }}{{ loop.revindex }} " +
                "{% endfor %}").render(new JtwigModelMap(Collections.singletonMap("list", (Object)Arrays.asList("a","b","c","d","e"))));
        assertThat(result, is("0145 1234 2323 3412 4501 "));
    }
    
    @Test
    public void iterateOnSequence () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("start", 'b')
                .withModelAttribute("end", 'l');
        String result = JtwigTemplate.inlineTemplate("{% for value in start..end %}{{ value }}{% endfor %}").render(model);
        assertThat(result, is("bcdefghijkl"));
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
}
