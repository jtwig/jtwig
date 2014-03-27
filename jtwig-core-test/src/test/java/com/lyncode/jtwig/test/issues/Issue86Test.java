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

package com.lyncode.jtwig.test.issues;

import com.lyncode.jtwig.test.AbstractJtwigTest;
import org.junit.Test;

import static com.lyncode.jtwig.SyntacticSugar.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class Issue86Test extends AbstractJtwigTest {

    @Test
    public void issue86() throws Exception {
        when(jtwigRenders(template("{{ -1 }}")));
        then(theRenderedTemplate(), is(equalTo("-1")));
    }
    @Test
    public void issue86WithConstant() throws Exception {
        when(jtwigRenders(template("{{ 1-1 }}")));
        then(theRenderedTemplate(), is(equalTo("0")));
    }
    @Test
    public void issue86WithVariable() throws Exception {
        given(theContext().withModelAttribute("value", 1));
        when(jtwigRenders(template("{{ value-1 }}")));
        then(theRenderedTemplate(), is(equalTo("0")));
    }
    @Test
    public void issue86WithVariableUnary() throws Exception {
        given(theContext().withModelAttribute("value", -1));
        when(jtwigRenders(template("{{ -value }}")));
        then(theRenderedTemplate(), is(equalTo("1")));
    }
}
