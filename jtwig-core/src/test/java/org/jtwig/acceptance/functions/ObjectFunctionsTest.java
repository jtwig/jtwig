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

public class ObjectFunctionsTest extends AbstractJtwigTest {

    @Test
    public void defaultFunctionTest() throws Exception {
        withResource("{{ default(null, 1) }}");
        then(theResult(), is(equalTo("1")));
    }

    @Test
    public void defaultUndefinedVariableTest() throws Exception {
        withResource("{{ default(a, 1) }}");
        then(theResult(), is(equalTo("1")));
    }

    @Test
    public void defaultUndefinedMethodOrFieldTest() throws Exception {
        given(theModel().withModelAttribute("a", new Object()));
        withResource("{{ default(a.call, 1) }}");
        then(theResult(), is(equalTo("1")));
    }

    @Test
    public void jsonEncode() throws Exception {
        withResource("{{ json_encode({one: 'hello'}) }}");
        then(theResult(), is(equalTo("{\"one\":\"hello\"}")));
    }

    @Test
    public void mapLength() throws Exception {
        withResource("{{ length({one: 'hello'}) }}");
        then(theResult(), is(equalTo("1")));
    }

    @Test
    public void listLength() throws Exception {
        withResource("{{ length([1,2]) }}");
        then(theResult(), is(equalTo("2")));
    }

    @Test
    public void stringLength() throws Exception {
        withResource("{{ length('Hello') }}");
        then(theResult(), is(equalTo("5")));
    }

    @Test
    public void mapFirst() throws Exception {
        withResource("{{ first({one: 'hello'}) }}");
        then(theResult(), is(equalTo("hello")));
    }

    @Test
    public void listFirst() throws Exception {
        withResource("{{ first([1,2]) }}");
        then(theResult(), is(equalTo("1")));
    }

    @Test
    public void stringFirst() throws Exception {
        withResource("{{ first('Hello') }}");
        then(theResult(), is(equalTo("H")));
    }

    @Test
    public void mapLast() throws Exception {
        withResource("{{ last({one: 'hello'}) }}");
        then(theResult(), is(equalTo("hello")));
    }

    @Test
    public void listLast() throws Exception {
        withResource("{{ last([1,2]) }}");
        then(theResult(), is(equalTo("2")));
    }

    @Test
    public void arrayLast() throws Exception {
        given(theModel().withModelAttribute("array", new int[]{1,2}));
        withResource("{{ last(array) }}");
        then(theResult(), is(equalTo("2")));
    }

    @Test
    public void stringLast() throws Exception {
        withResource("{{ last('Hello') }}");
        then(theResult(), is(equalTo("o")));
    }

    @Test
    public void reserveTest() throws Exception {
        withResource("{{ reverse('Hello') }}");
        then(theResult(), is(equalTo("olleH")));
    }
    @Test
    public void reserveListTest() throws Exception {
        withResource("{{ reverse([1,2]) }}");
        then(theResult(), is(equalTo("[2, 1]")));
    }
}
