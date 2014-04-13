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

public class ObjectFunctionsTest extends AbstractJtwigTest {

    @Test
    public void defaultFunctionTest() throws Exception {
        when(jtwigRenders(template("{{ default(null, 1) }}")));
        then(theRenderedTemplate(), is(equalTo("1")));
    }

    @Test
    public void defaultUndefinedVariableTest() throws Exception {
        when(jtwigRenders(template("{{ default(a, 1) }}")));
        then(theRenderedTemplate(), is(equalTo("1")));
    }

    @Test
    public void defaultUndefinedMethodOrFieldTest() throws Exception {
        given(theContext().withModelAttribute("a", new Object()));
        when(jtwigRenders(template("{{ default(a.call, 1) }}")));
        then(theRenderedTemplate(), is(equalTo("1")));
    }

    @Test
    public void jsonEncode() throws Exception {
        when(jtwigRenders(template("{{ json_encode({one: 'hello'}) }}")));
        then(theRenderedTemplate(), is(equalTo("{\"one\":\"hello\"}")));
    }

    @Test
    public void mapLength() throws Exception {
        when(jtwigRenders(template("{{ length({one: 'hello'}) }}")));
        then(theRenderedTemplate(), is(equalTo("1")));
    }

    @Test
    public void listLength() throws Exception {
        when(jtwigRenders(template("{{ length([1,2]) }}")));
        then(theRenderedTemplate(), is(equalTo("2")));
    }

    @Test
    public void stringLength() throws Exception {
        when(jtwigRenders(template("{{ length('Hello') }}")));
        then(theRenderedTemplate(), is(equalTo("5")));
    }

    @Test
    public void mapFirst() throws Exception {
        when(jtwigRenders(template("{{ first({one: 'hello'}) }}")));
        then(theRenderedTemplate(), is(equalTo("hello")));
    }

    @Test
    public void listFirst() throws Exception {
        when(jtwigRenders(template("{{ first([1,2]) }}")));
        then(theRenderedTemplate(), is(equalTo("1")));
    }

    @Test
    public void stringFirst() throws Exception {
        when(jtwigRenders(template("{{ first('Hello') }}")));
        then(theRenderedTemplate(), is(equalTo("H")));
    }

    @Test
    public void mapLast() throws Exception {
        when(jtwigRenders(template("{{ last({one: 'hello'}) }}")));
        then(theRenderedTemplate(), is(equalTo("hello")));
    }

    @Test
    public void listLast() throws Exception {
        when(jtwigRenders(template("{{ last([1,2]) }}")));
        then(theRenderedTemplate(), is(equalTo("2")));
    }

    @Test
    public void arrayLast() throws Exception {
        given(theContext().withModelAttribute("array", new int[]{1,2}));
        when(jtwigRenders(template("{{ last(array) }}")));
        then(theRenderedTemplate(), is(equalTo("2")));
    }

    @Test
    public void stringLast() throws Exception {
        when(jtwigRenders(template("{{ last('Hello') }}")));
        then(theRenderedTemplate(), is(equalTo("o")));
    }

    @Test
    public void reserveTest() throws Exception {
        when(jtwigRenders(template("{{ reverse('Hello') }}")));
        then(theRenderedTemplate(), is(equalTo("olleH")));
    }
    @Test
    public void reserveListTest() throws Exception {
        when(jtwigRenders(template("{{ reverse([1,2]) }}")));
        then(theRenderedTemplate(), is(equalTo("[2, 1]")));
    }
}
