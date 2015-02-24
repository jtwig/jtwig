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

package org.jtwig.acceptance.functions;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jtwig.util.SyntacticSugar.given;

public class ObjectFunctionsTest {

    @Test
    public void defaultFunctionTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ default(null, 1) }}")
            .render(model);

        assertThat(result, is(equalTo("1")));
    }

    @Test
    public void defaultUndefinedVariableTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ default(a, 1) }}")
            .render(model);

        assertThat(result, is(equalTo("1")));
    }

    @Test
    public void defaultUndefinedMethodOrFieldTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap()
            .withModelAttribute("a", new Object());

        String result = JtwigTemplate
            .inlineTemplate("{{ default(a.call, 1) }}")
            .render(model);

        assertThat(result, is(equalTo("1")));
    }

    @Test
    public void jsonEncode() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ json_encode({one: 'hello'}) }}")
            .render(model);

        assertThat(result, is(equalTo("{\"one\":\"hello\"}")));
    }

    @Test
    public void mapLength() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ length({one: 'hello'}) }}")
            .render(model);

        assertThat(result, is(equalTo("1")));
    }

    @Test
    public void listLength() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ length([1,2]) }}")
            .render(model);

        assertThat(result, is(equalTo("2")));
    }

    @Test
    public void stringLength() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ length('Hello') }}")
            .render(model);

        assertThat(result, is(equalTo("5")));
    }

    @Test
    public void mapFirst() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ first({one: 'hello'}) }}")
            .render(model);

        assertThat(result, is(equalTo("hello")));
    }

    @Test
    public void listFirst() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ first([1,2]) }}")
            .render(model);

        assertThat(result, is(equalTo("1")));
    }

    @Test
    public void stringFirst() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ first('Hello') }}")
            .render(model);

        assertThat(result, is(equalTo("H")));
    }

    @Test
    public void mapLast() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ last({one: 'hello'}) }}")
            .render(model);

        assertThat(result, is(equalTo("hello")));
    }

    @Test
    public void listLast() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ last([1,2]) }}")
            .render(model);

        assertThat(result, is(equalTo("2")));
    }

    @Test
    public void arrayLast() throws Exception {
        JtwigModelMap model = new JtwigModelMap().withModelAttribute("array", new int[]{1,2});

        String result = JtwigTemplate
            .inlineTemplate("{{ last(array) }}")
            .render(model);

        assertThat(result, is(equalTo("2")));
    }

    @Test
    public void stringLast() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ last('Hello') }}")
            .render(model);

        assertThat(result, is(equalTo("o")));
    }

    @Test
    public void reserveTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ reverse('Hello') }}")
            .render(model);

        assertThat(result, is(equalTo("olleH")));
    }
    @Test
    public void reserveListTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ reverse([1,2]) }}")
            .render(model);

        assertThat(result, is(equalTo("[2, 1]")));
    }
}
