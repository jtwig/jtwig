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
}
