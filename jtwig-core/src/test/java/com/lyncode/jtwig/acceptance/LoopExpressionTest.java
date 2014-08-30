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

import org.junit.Test;

import java.util.HashMap;

import static com.lyncode.jtwig.util.SyntacticSugar.*;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class LoopExpressionTest extends AbstractJtwigTest {
    @Test
    public void simpleLoop() throws Exception {
        given(aModel().withModelAttribute("list", asList("a", "b", "c")));
        when(jtwigRenders(template("{% for item in list %}{{ item }}{% endfor %}")));
        then(theRenderedTemplate(), is(equalTo("abc")));
    }
    @Test
    public void simpleLoopLength() throws Exception {
        given(aModel().withModelAttribute("list", asList("a", "b", "c")));
        when(jtwigRenders(template("{% for item in list %}{{ loop.length }}{% endfor %}")));
        then(theRenderedTemplate(), is(equalTo("333")));
    }
    @Test
    public void simpleMapLoopWithList() throws Exception {
        given(aModel().withModelAttribute("list", asList("a")));
        when(jtwigRenders(template("{% for key, value in list %}{{ key }} - {{ value }}{% endfor %}")));
        then(theRenderedTemplate(), is(equalTo("0 - a")));
    }
    @Test
    public void emptyMap() throws Exception {
        given(aModel().withModelAttribute("list", new HashMap<>()));
        when(jtwigRenders(template("{% for key, value in list %}{{ key }} - {{ value }}{% else %}Test{% endfor %}")));
        then(theRenderedTemplate(), is(equalTo("Test")));
    }
}
