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

import org.jtwig.AbstractJtwigTest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.jtwig.util.SyntacticSugar.*;

public class StringFunctionsTest extends AbstractJtwigTest {
    @Test
    public void capitalize() throws Exception {
        withResource("{{ capitalize('joao') }}");
        then(theResult(), is(equalTo("Joao")));
    }

    @Test
    public void convertEncoding() throws Exception {
        withResource("{{ convert_encoding('joao', 'UTF-8', 'ASCII') }}");
        then(theResult(), is(equalTo("joao")));
    }

    @Test
    public void escape() throws Exception {
        withResource("{{ escape('joão') }}");
        then(theResult(), is(equalTo("jo&atilde;o")));
    }

    @Test
    public void escapeJavascript() throws Exception {
        withResource("{{ escape('\"', 'js') }}");
        then(theResult(), is(equalTo("\\\"")));
    }

    @Test
    public void formatFunctionTest() throws Exception {
        withResource("{{ format('Hello %s', 'Joao') }}");
        then(theResult(), is(equalTo("Hello Joao")));
    }

    @Test
    public void formatFunctionDoubleTest() throws Exception {
        withResource("{{ format('Hello %s %s', 'Joao', 'One') }}");
        then(theResult(), is(equalTo("Hello Joao One")));
    }

    @Test
    public void lowerFunctionTest() throws Exception {
        withResource("{{ lower('Hi') }}");
        then(theResult(), is(equalTo("hi")));
    }

    @Test
    public void upperFunctionTest() throws Exception {
        withResource("{{ upper('Hi') }}");
        then(theResult(), is(equalTo("HI")));
    }

    @Test
    public void nl2brTest() throws Exception {
        given(theModel().withModelAttribute("var", "Hi\n\n"));
        withResource("{{ nl2br(var) }}");
        then(theResult(), is(equalTo("Hi<br /><br />")));
    }

    @Test
    public void replaceTest() throws Exception {
        withResource("{{ replace('Hi var', { var: 'Joao' }) }}");
        then(theResult(), is(equalTo("Hi Joao")));
    }

    @Test
    public void splitTest() throws Exception {
        withResource("{{ split('one, two, three', ',').get(0) }}");
        then(theResult(), is(equalTo("one")));
    }

    @Test
    public void stripTagsTest() throws Exception {
        withResource("{{ striptags('<p>Hi Joao</p>') }}");
        then(theResult(), is(equalTo("Hi Joao")));
    }

    @Test
    public void stripTagsWithAllowedTest() throws Exception {
        withResource("{{ striptags('<a><p>Hi Joao</p></a>', 'p') }}");
        then(theResult(), is(equalTo("<p>Hi Joao</p>")));
    }

    @Test
    public void titleTest() throws Exception {
        withResource("{{ title('hi joao') }}");
        then(theResult(), is(equalTo("Hi Joao")));
    }

    @Test
    public void trimTest() throws Exception {
        withResource("{{ trim(' joao ') }}");
        then(theResult(), is(equalTo("joao")));
    }

    @Test
    public void urlEncodeTest() throws Exception {
        withResource("{{ url_encode('ã') }}");
        then(theResult(), is(equalTo("%C3%A3")));
    }

    @Test
    public void urlEncodeMapTest() throws Exception {
        withResource("{{ url_encode({ one: 1, two: 2 }) }}");
        then(theResult(), is(equalTo("one=1&two=2")));
    }
}
