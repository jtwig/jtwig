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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

public class IsOperatorTest {
    public static final String HELLO = "one";
    
    @Test
    public void isOperatorEmptyFunction () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if ([] is empty) %}Hi{% endif %}")
            .render(model);

        assertThat(result, is(equalTo("Hi")));
    }

    @Test
    public void isOperatorConstantFunction () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if ('one' is constant('org.jtwig.acceptance.IsOperatorTest.HELLO')) %}Hi{% endif %}")
            .render(model);

        assertThat(result, is(equalTo("Hi")));
    }

    @Test
    public void isDefinedFunction () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if (variable is not defined) %}Hi{% endif %}")
            .render(model);

        assertThat(result, is(equalTo("Hi")));
    }
    @Test
    public void isEvenFunction () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if (1 is not even) %}Hi{% endif %}")
            .render(model);

        assertThat(result, is(equalTo("Hi")));
    }
    @Test
    public void isNotOddFunction () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if (1 is not odd) %}Hi{% endif %}")
            .render(model);

        assertThat(result, is(equalTo("")));
    }
    @Test
    public void isOddFunction () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if (1 is odd) %}Hi{% endif %}")
            .render(model);

        assertThat(result, is(equalTo("Hi")));
    }
    @Test
    public void isIterableFunction () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if ([] is iterable) %}Hi{% endif %}")
            .render(model);

        assertThat(result, is(equalTo("Hi")));
    }
    @Test
    public void isIterableArrayFunction () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.add("value", new Object[0]);

        String result = JtwigTemplate
            .inlineTemplate("{% if (value is iterable) %}Hi{% endif %}")
            .render(model);

        assertThat(result, is(equalTo("Hi")));
    }
    @Test
    public void isIterableMapFunction () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("value", Collections.EMPTY_MAP);

        String result = JtwigTemplate
            .inlineTemplate("{% if (value is iterable) %}Hi{% endif %}")
            .render(model);

        assertThat(result, is(equalTo("Hi")));
    }

    @Test
    public void isNullFunction () throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("value", null);

        String result = JtwigTemplate
            .inlineTemplate("{% if (value is null) %}Hi{% endif %}")
            .render(model);

        assertThat(result, is(equalTo("Hi")));
    }

    @Test
    public void isDivisableFunction () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if (3 is divisable by 1) %}Hi{% else %}OH{% endif %}")
            .render(model);

        assertThat(result, is(equalTo("Hi")));
    }
}
