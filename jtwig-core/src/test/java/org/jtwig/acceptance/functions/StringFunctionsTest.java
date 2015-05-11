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

public class StringFunctionsTest {
    @Test
    public void capitalize() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ capitalize('joao') }}")
            .render(model);

        assertThat(result, is(equalTo("Joao")));
    }

    @Test
    public void convertEncoding() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ convert_encoding('joao', 'UTF-8', 'ASCII') }}")
            .render(model);

        assertThat(result, is(equalTo("joao")));
    }

    @Test
    public void escape() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ escape('joão') }}")
            .render(model);

        assertThat(result, is(equalTo("jo&atilde;o")));
    }

    @Test
    public void escapeJavascript() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ escape('\"', 'js') }}")
            .render(model);

        assertThat(result, is(equalTo("\\\"")));
    }

    @Test
    public void formatFunctionTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ format('Hello %s', 'Joao') }}")
            .render(model);

        assertThat(result, is(equalTo("Hello Joao")));
    }

    @Test
    public void formatFunctionDoubleTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ format('Hello %s %s', 'Joao', 'One') }}")
            .render(model);

        assertThat(result, is(equalTo("Hello Joao One")));
    }

    @Test
    public void lowerFunctionTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ lower('Hi') }}")
            .render(model);

        assertThat(result, is(equalTo("hi")));
    }

    @Test
    public void upperFunctionTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ upper('Hi') }}")
            .render(model);

        assertThat(result, is(equalTo("HI")));
    }

    @Test
    public void nl2brTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("var", "Hi\n\n");

        String result = JtwigTemplate
            .inlineTemplate("{{ nl2br(var) }}")
            .render(model);

        assertThat(result, is(equalTo("Hi<br /><br />")));
    }

    @Test
    public void replaceTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ replace('Hi var', { var: 'Joao' }) }}")
            .render(model);

        assertThat(result, is(equalTo("Hi Joao")));
    }

    @Test
    public void splitTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ split('one, two, three', ',').get(0) }}")
            .render(model);

        assertThat(result, is(equalTo("one")));
    }

    @Test
    public void stripTagsTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ striptags('<p>Hi Joao</p>') }}")
            .render(model);

        assertThat(result, is(equalTo("Hi Joao")));
    }

    @Test
    public void stripTagsWithAllowedTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ striptags('<a><p>Hi Joao</p></a>', 'p') }}")
            .render(model);

        assertThat(result, is(equalTo("<p>Hi Joao</p>")));
    }

    @Test
    public void titleTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ title('hi joao') }}")
            .render(model);

        assertThat(result, is(equalTo("Hi Joao")));
    }

    @Test
    public void trimTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ trim(' joao ') }}")
            .render(model);

        assertThat(result, is(equalTo("joao")));
    }

    @Test
    public void urlEncodeTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ url_encode('ã') }}")
            .render(model);

        assertThat(result, is(equalTo("%C3%A3")));
    }

    @Test
    public void urlEncodeMapTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ url_encode({ one: 1, two: 2 }) }}")
            .render(model);

        assertThat(result, is(equalTo("one=1&two=2")));
    }
}
