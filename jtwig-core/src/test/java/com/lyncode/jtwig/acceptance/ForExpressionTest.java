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

package com.lyncode.jtwig.acceptance;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ForExpressionTest {
    @Test
    public void emptyListShouldOutputNothing () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{% for item in list %}Item {{ item }}{% endfor %}");
        JtwigModelMap context = new JtwigModelMap();
        ArrayList<String> value = new ArrayList<String>();
        context.withModelAttribute("list", value);
        assertThat(template.output(context), is(""));
    }
    @Test
    public void nonEmptyListShouldOutputSomething () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{% for item in list %}Item {{ item }}{% endfor %}");
        JtwigModelMap context = new JtwigModelMap();
        ArrayList<String> value = new ArrayList<String>();
        value.add("a");
        context.withModelAttribute("list", value);
        assertThat(template.output(context), is("Item a"));
    }

    @Test
    public void forLoopMustExposeTheLoopVariable () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{% for item in list %}" +
                "{% if loop.first %}First {% elseif loop.last %}Last{% else %}I: {{ loop.index0 }} R: {{ loop.revindex0 }} {% endif %}" +
                "{% endfor %}");
        JtwigModelMap context = new JtwigModelMap();
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
    public void ensureProperLoopVariableIndexing () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{% for item in list %}" +
                "{{ loop.index0 }}{{ loop.index }}{{ loop.revindex0 }}{{ loop.revindex }} " +
                "{% endfor %}");
        JtwigModelMap context = new JtwigModelMap();
        ArrayList<String> value = new ArrayList<String>();
        value.add("a");
        value.add("b");
        value.add("c");
        value.add("d");
        value.add("e");
        context.withModelAttribute("list", value);
        assertThat(template.output(context), is("0145 1234 2323 3412 4501 "));
    }

    @Test
    public void shouldNotOutputNothingIfListIsNull () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{% for item in list %}a{% endfor %}");
        JtwigModelMap context = new JtwigModelMap();
        context.withModelAttribute("list", null);
        assertThat(template.output(context), is(""));
    }


    @Test
    public void iterateOverMap () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{% for key, value in map %}{{ key }} = {{ value }}|{% endfor %}");
        JtwigModelMap context = new JtwigModelMap();
        LinkedHashMap<String, String> value = new  LinkedHashMap<String, String>();
        value.put("one", "1");
        value.put("two", "2");
        value.put("three", "3");
        context.withModelAttribute("map", value);
        assertThat(template.output(context), is("one = 1|two = 2|three = 3|"));
    }
    
    @Test
    public void avoidElseOnPopulatedList () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{% for value in list %}{{ value }}{% else %}nothing{% endfor %}");
        JtwigModelMap context = new JtwigModelMap();
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        context.withModelAttribute("list", list);
        assertThat(template.output(context), is("ab"));
    }
    
    @Test
    public void outputElseOnEmptyList () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{% for value in list %}item{% else %}nothing{% endfor %}");
        JtwigModelMap context = new JtwigModelMap();
        List list = Collections.EMPTY_LIST;
        context.withModelAttribute("list", list);
        assertThat(template.output(context), is("nothing"));
    }
    
    @Test
    public void outputElseOnUndefined () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{% for value in var %}{{ value }}{% else %}nothing{% endfor %}");
        JtwigModelMap context = new JtwigModelMap();
        assertThat(template.output(context), is("nothing"));
    }
    
    @Test
    public void iterateOnSelection () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{% for value in obj.getList(name) %}{{ value }}{% endfor %}");
        JtwigModelMap context = new JtwigModelMap();
        context.withModelAttribute("obj", new Obj());
        context.withModelAttribute("name", "Test");
        assertThat(template.output(context), is("ab"));
    }
    
    @Test
    public void iterateOnSequence () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{% for value in start..end %}{{ value }}{% endfor %}");
        JtwigModelMap context = new JtwigModelMap();
        context.withModelAttribute("start", 'b').withModelAttribute("end", 'l');
        assertThat(template.output(context), is("bcdefghijkl"));
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
