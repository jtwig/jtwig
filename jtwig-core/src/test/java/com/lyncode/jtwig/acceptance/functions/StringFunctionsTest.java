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

package com.lyncode.jtwig.acceptance.functions;

import com.lyncode.jtwig.acceptance.AbstractJtwigTest;
import org.junit.Test;

import static com.lyncode.jtwig.util.SyntacticSugar.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class StringFunctionsTest extends AbstractJtwigTest {
    @Test
    public void capitalize() throws Exception {
        when(jtwigRenders(template("{{ capitalize('joao') }}")));
        then(theRenderedTemplate(), is(equalTo("Joao")));
    }

    @Test
    public void convertEncoding() throws Exception {
        when(jtwigRenders(template("{{ convert_encoding('joao', 'UTF-8', 'ASCII') }}")));
        then(theRenderedTemplate(), is(equalTo("joao")));
    }

    @Test
    public void escape() throws Exception {
        when(jtwigRenders(template("{{ escape('joão') }}")));
        then(theRenderedTemplate(), is(equalTo("jo&atilde;o")));
    }

    @Test
    public void escapeJavascript() throws Exception {
        when(jtwigRenders(template("{{ escape('\"', 'js') }}")));
        then(theRenderedTemplate(), is(equalTo("\\\"")));
    }

    @Test
    public void formatFunctionTest() throws Exception {
        when(jtwigRenders(template("{{ format('Hello %s', 'Joao') }}")));
        then(theRenderedTemplate(), is(equalTo("Hello Joao")));
    }

    @Test
    public void formatFunctionDoubleTest() throws Exception {
        when(jtwigRenders(template("{{ format('Hello %s %s', 'Joao', 'One') }}")));
        then(theRenderedTemplate(), is(equalTo("Hello Joao One")));
    }

    @Test
    public void lowerFunctionTest() throws Exception {
        when(jtwigRenders(template("{{ lower('Hi') }}")));
        then(theRenderedTemplate(), is(equalTo("hi")));
    }

    @Test
    public void upperFunctionTest() throws Exception {
        when(jtwigRenders(template("{{ upper('Hi') }}")));
        then(theRenderedTemplate(), is(equalTo("HI")));
    }

    @Test
    public void nl2brTest() throws Exception {
        given(aModel().withModelAttribute("var", "Hi\n\n"));
        when(jtwigRenders(template("{{ nl2br(var) }}")));
        then(theRenderedTemplate(), is(equalTo("Hi<br /><br />")));
    }

    @Test
    public void replaceTest() throws Exception {
        when(jtwigRenders(template("{{ replace('Hi var', { var: 'Joao' }) }}")));
        then(theRenderedTemplate(), is(equalTo("Hi Joao")));
    }

    @Test
    public void splitTest() throws Exception {
        when(jtwigRenders(template("{{ split('one, two, three', ',').get(0) }}")));
        then(theRenderedTemplate(), is(equalTo("one")));
    }

    @Test
    public void stripTagsTest() throws Exception {
        when(jtwigRenders(template("{{ striptags('<p>Hi Joao</p>') }}")));
        then(theRenderedTemplate(), is(equalTo("Hi Joao")));
    }

    @Test
    public void stripTagsWithAllowedTest() throws Exception {
        when(jtwigRenders(template("{{ striptags('<a><p>Hi Joao</p></a>', 'p') }}")));
        then(theRenderedTemplate(), is(equalTo("<p>Hi Joao</p>")));
    }

    @Test
    public void titleTest() throws Exception {
        when(jtwigRenders(template("{{ title('hi joao') }}")));
        then(theRenderedTemplate(), is(equalTo("Hi Joao")));
    }

    @Test
    public void trimTest() throws Exception {
        when(jtwigRenders(template("{{ trim(' joao ') }}")));
        then(theRenderedTemplate(), is(equalTo("joao")));
    }

    @Test
    public void urlEncodeTest() throws Exception {
        when(jtwigRenders(template("{{ url_encode('ã') }}")));
        then(theRenderedTemplate(), is(equalTo("%C3%A3")));
    }

    @Test
    public void urlEncodeMapTest() throws Exception {
        when(jtwigRenders(template("{{ url_encode({ one: 1, two: 2 }) }}")));
        then(theRenderedTemplate(), is(equalTo("one=1&two=2")));
    }
}
