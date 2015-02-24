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

package org.jtwig.acceptance.issues;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Issue86Test {

    @Test
    public void issue86() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ -1 }}")
            .render(model);

        assertThat(result, is(equalTo("-1")));
    }

    @Test
    public void issue86WithConstant() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ 1-1 }}")
            .render(model);

        assertThat(result, is(equalTo("0")));
    }

    @Test
    public void issue86WithVariable() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("value", 1);

        String result = JtwigTemplate
            .inlineTemplate("{{ value-1 }}")
            .render(model);

        assertThat(result, is(equalTo("0")));
    }

    @Test
    public void issue86WithVariableUnary() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("value", -1);

        String result = JtwigTemplate
            .inlineTemplate("{{ -value }}")
            .render(model);

        assertThat(result, is(equalTo("1")));
    }
}
