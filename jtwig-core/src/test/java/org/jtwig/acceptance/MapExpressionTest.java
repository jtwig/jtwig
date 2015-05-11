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

package org.jtwig.acceptance;

import java.util.Collections;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

public class MapExpressionTest {
    @Test
    public void mapWithStringLiteralsAsKey () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% set a = { 'test two': 'details' } %}{{ a['test two'] }}")
            .render(model);

        assertThat(result, is(equalTo("details")));
    }

    @Test
    public void ifWithEmptyMapShouldBeTheSameAsFalse () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("map", Collections.EMPTY_MAP);

        String result = JtwigTemplate
            .inlineTemplate("{% if (map) %}not empty{% else %}empty{% endif %}")
            .render(model);

        assertThat(result, is(equalTo("empty")));
    }

    @Test
    public void ifNoKeyInMapTryMethods () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("map", Collections.EMPTY_MAP);

        String result = JtwigTemplate
            .inlineTemplate("{{ map.size }}")
            .render(model);

        assertThat(result, is(equalTo("0")));
    }

    @Test
    public void methodsAndFieldsShouldPrevail () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("map", Collections.singletonMap("size", "Hello!"));

        String result = JtwigTemplate
            .inlineTemplate("{{ map.size }}")
            .render(model);

        assertThat(result, is(equalTo("1")));
    }
}
