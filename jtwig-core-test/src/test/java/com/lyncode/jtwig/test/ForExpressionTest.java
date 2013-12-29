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

package com.lyncode.jtwig.test;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ForExpressionTest {
    @Test
    public void emptyListShouldOutputNothing () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% for item in list %}Item {{ item }}{% endfor %}");
        JtwigContext context = new JtwigContext();
        ArrayList<String> value = new ArrayList<String>();
        context.withModelAttribute("list", value);
        assertThat(template.output(context), is(""));
    }
    @Test
    public void nonEmptyListShouldOutputSomething () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% for item in list %}Item {{ item }}{% endfor %}");
        JtwigContext context = new JtwigContext();
        ArrayList<String> value = new ArrayList<String>();
        value.add("a");
        context.withModelAttribute("list", value);
        assertThat(template.output(context), is("Item a"));
    }
    @Test
    public void forLoopMustExposeTheLoopVariable () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% for item in list %}" +
                "{% if loop.first %}First {% elseif loop.last %}Last{% else %}I: {{ loop.index }} R: {{ loop.revindex }} {% endif %}" +
                "{% endfor %}");
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
    public void shouldNotOutputNothingIfListIsNull () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% for item in list %}a{% endfor %}");
        JtwigContext context = new JtwigContext();
        context.withModelAttribute("list", null);
        assertThat(template.output(context), is(""));
    }


    @Test
    public void iterateOverMap () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% for key, value in map %}{{ key }} = {{ value }}|{% endfor %}");
        JtwigContext context = new JtwigContext();
        LinkedHashMap<String, String> value = new  LinkedHashMap<String, String>();
        value.put("one", "1");
        value.put("two", "2");
        value.put("three", "3");
        context.withModelAttribute("map", value);
        assertThat(template.output(context), is("one = 1|two = 2|three = 3|"));
    }
}
