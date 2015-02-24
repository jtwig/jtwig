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

public class OutputTest {
    @Test
    public void shouldAllowConcatenationOfDistinctElements () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("list", Collections.EMPTY_LIST);

        String result = JtwigTemplate
            .inlineTemplate("{{ concat ('1', list.size ,'3') }}")
            .render(model);

        assertThat(result, is(equalTo("103")));
    }

    @Test
    public void shouldAllowFilters () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ ['1', '2' ,'3'] | join(',') }}")
            .render(model);

        assertThat(result, is(equalTo("1,2,3")));
    }
}
