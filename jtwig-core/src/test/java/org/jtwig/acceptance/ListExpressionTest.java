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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

public class ListExpressionTest {
    @Test
    public void integerListByComprehension () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ (1..5) | join(',') }}")
            .render(model);

        assertThat(result, is(equalTo("1,2,3,4,5")));
    }
    @Test
    public void integerListByComprehensionReverse () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ (5..1) | join(',') }}")
            .render(model);

        assertThat(result, is(equalTo("5,4,3,2,1")));
    }

    @Test
    public void characterListByComprehension () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ ('a'..'c') | join }}")
            .render(model);

        assertThat(result, is(equalTo("abc")));
    }

    @Test
    public void characterListByComprehensionReverse () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ ('c'.. 'a') | join }}")
            .render(model);

        assertThat(result, is(equalTo("cba")));
    }
}