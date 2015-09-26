/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.acceptance.extension.core.filters;

import java.util.Collections;
import java.util.HashSet;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.jtwig.JtwigModelMap;
import static org.jtwig.JtwigTemplate.inlineTemplate;
import org.jtwig.exception.RenderException;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class LastFilterTest {
    
    @Test
    public void generalTests() throws Exception {
        assertThat(inlineTemplate("{{ null|last }}").render(), is(equalTo("")));
        assertThat(inlineTemplate("{{ {'a':'1','b':'2'}|last }}").render(), is(equalTo("2")));
        assertThat(inlineTemplate("{{ ['a','b']|last }}").render(), is(equalTo("b")));
        assertThat(inlineTemplate("{{ []|last }}").render(), is(equalTo("")));
        assertThat(inlineTemplate("{{ set|last }}").render(new JtwigModelMap(Collections.singletonMap("set", (Object)new HashSet()))), is(equalTo("")));
        assertThat(inlineTemplate("{{ 'abc'|last }}").render(), is(equalTo("c")));
        assertThat(inlineTemplate("{{ ''|last }}").render(), is(equalTo("")));
        assertThat(inlineTemplate("{{ 123|last }}").render(), is(equalTo("3")));
        assertThat(inlineTemplate("{{ 1.23|last }}").render(), is(equalTo("3")));
        assertThat(inlineTemplate("{{ true|last }}").render(), is(equalTo("1")));
        assertThat(inlineTemplate("{{ false|last }}").render(), is(equalTo("0")));
    }
    
    @Test(expected = RenderException.class)
    public void lastOfUnknownTypeThrowsException() throws Exception {
        inlineTemplate("{{ obj|last }}").render(new JtwigModelMap(Collections.singletonMap("obj", new Object())));
    }
}