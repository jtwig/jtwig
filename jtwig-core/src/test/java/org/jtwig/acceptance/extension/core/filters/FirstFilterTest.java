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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.jtwig.JtwigModelMap;
import static org.jtwig.JtwigTemplate.inlineTemplate;
import org.jtwig.exception.RenderException;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class FirstFilterTest {
    
    @Test
    public void generalTests() throws Exception {
        assertThat(inlineTemplate("{{ 5678|first }}").render(),
                is(equalTo("5")));
        assertThat(inlineTemplate("{{ 5.678|first }}").render(),
                is(equalTo("5")));
        assertThat(inlineTemplate("{{ 'test'|first }}").render(),
                is(equalTo("t")));
        assertThat(inlineTemplate("{{ ['a','b','c']|first }}").render(),
                is(equalTo("a")));
        assertThat(inlineTemplate("{{ []|first }}").render(),
                is(equalTo("")));
        assertThat(inlineTemplate("{{ {'1':'a','2':'b','3':'c'}|first }}").render(),
                is(equalTo("a")));
        assertThat(inlineTemplate("{{ ''|first }}").render(),
                is(equalTo("")));
        assertThat(inlineTemplate("{{ null|first }}").render(),
                is(equalTo("")));
        assertThat(inlineTemplate("{{ true|first }}").render(),
                is(equalTo("1")));
    }
    
    @Test(expected = RenderException.class)
    public void firstOfUnknownTypeThrowsException() throws Exception {
        inlineTemplate("{{ obj|first }}").render(new JtwigModelMap(Collections.singletonMap("obj", new Object())));
    }
    
}