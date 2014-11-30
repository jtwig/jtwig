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

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ParseException;
import org.jtwig.exception.RenderException;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class IfExpressionTest extends AbstractJtwigTest {
    @Test
    public void ifWithEmptyListShouldBeTheSameAsFalse () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{% if (list) %}not empty{% else %}empty{% endif %}");
        JtwigModelMap context = new JtwigModelMap();
        context.withModelAttribute("list", new ArrayList<String>());
        assertThat(template.output(context), is("empty"));
    }

    @Test
    public void ifInOperator () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{% if (1 in [1, 2]) %}ok{% else %}ko{% endif %}");
        JtwigModelMap context = new JtwigModelMap();
        assertThat(template.output(context), is("ok"));
    }


    @Test
    public void ifNotInOperator () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{% if (1 not in [3, 2]) %}ok{% else %}ko{% endif %}");
        JtwigModelMap context = new JtwigModelMap();
        assertThat(template.output(context), is("ok"));
    }

    @Test
    public void ifWithNonEmptyListShouldBeTheSameAsTrue () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{% if (list) %}not empty{% else %}empty{% endif %}");
        JtwigModelMap context = new JtwigModelMap();
        ArrayList<String> value = new ArrayList<String>();
        value.add("a");
        context.withModelAttribute("list", value);
        assertThat(template.output(context), is("not empty"));
    }

    @Test
    public void IfWithContentInside () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{% if (items) %}" +
                "Items: "+
                "{% for item in items %}" +
                "{{ item }}" +
                "{% endfor %}" +
                "{% endif %}");

        JtwigModelMap context = new JtwigModelMap();
        ArrayList<String> value = new ArrayList<String>();
        value.add("a");
        context.withModelAttribute("items", value);
        assertThat(template.output(context), is("Items: a"));
    }
}
