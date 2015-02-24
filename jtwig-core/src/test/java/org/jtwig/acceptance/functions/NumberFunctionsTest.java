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

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NumberFunctionsTest {
    @Test
    public void testDefaultBehaviorWithoutSeparators() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ number_format(100000.1) }}")
            .render(model);

        assertThat(result, is(equalTo("100000.1")));
    }

    @Test
    public void testWithTwoDecimalDigits() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ number_format(100000.1, 2) }}")
            .render(model);

        assertThat(result, is(equalTo("100000.10")));
    }
    @Test
    public void testWithCommaSeparatingDecimal() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ number_format(100000.1, 2, ',') }}")
            .render(model);

        assertThat(result, is(equalTo("100000,10")));
    }
    @Test
    public void testWithThousandsSeparator() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ number_format(100000.1, 2, ',', ' ') }}")
            .render(model);

        assertThat(result, is(equalTo("100 000,10")));
    }
    @Test
    public void rangeTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ range(1,3) }}")
            .render(model);

        assertThat(result, is(equalTo("[1, 2, 3]")));
    }
    @Test
    public void rangeStepTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ range(1, 3, 2) }}")
            .render(model);

        assertThat(result, is(equalTo("[1, 3]")));
    }

    @Test
    public void rangeCharTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ range('a', 'c') }}")
            .render(model);

        assertThat(result, is(equalTo("[a, b, c]")));
    }
    @Test
    public void rangeStringTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ range(\"AA\", \"BZ\") }}")
            .render(model);

        assertThat(result, is(equalTo("[A, B]")));
    }
}
