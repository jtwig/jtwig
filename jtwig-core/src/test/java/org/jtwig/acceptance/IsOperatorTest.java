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

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class IsOperatorTest extends AbstractJtwigTest {
    public static final String HELLO = "one";
    private JtwigModelMap context = new JtwigModelMap();

    @Test
    public void isOperatorEmptyFunction () throws Exception {
        JtwigTemplate template = JtwigTemplate.fromString("{% if ([] is empty) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }

    @Test
    public void isOperatorConstantFunction () throws Exception {
        JtwigTemplate template = JtwigTemplate.fromString("{% if ('one' is constant('org.jtwig.acceptance.IsOperatorTest.HELLO')) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }

    @Test
    public void isDefinedFunction () throws Exception {
        JtwigTemplate template = JtwigTemplate.fromString("{% if (variable is not defined) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }
    @Test
    public void isEvenFunction () throws Exception {
        JtwigTemplate template = JtwigTemplate.fromString("{% if (1 is not even) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }
    @Test
    public void isNotOddFunction () throws Exception {
        JtwigTemplate template = JtwigTemplate.fromString("{% if (1 is not odd) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("")));
    }
    @Test
    public void isOddFunction () throws Exception {
        JtwigTemplate template = JtwigTemplate.fromString("{% if (1 is odd) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }
    @Test
    public void isIterableFunction () throws Exception {
        JtwigTemplate template = JtwigTemplate.fromString("{% if ([] is iterable) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }
    @Test
    public void isIterableArrayFunction () throws Exception {
        context.add("value", new Object[0]);
        JtwigTemplate template = JtwigTemplate.fromString("{% if (value is iterable) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }
    @Test
    public void isIterableMapFunction () throws Exception {
        context.withModelAttribute("value", new HashMap<>());
        JtwigTemplate template = JtwigTemplate.fromString("{% if (value is iterable) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }

    @Test
    public void isNullFunction () throws Exception {
        context.withModelAttribute("value", null);
        JtwigTemplate template = JtwigTemplate.fromString("{% if (value is null) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }

    @Test
    public void isDivisableFunction () throws Exception {
        JtwigTemplate template = JtwigTemplate.fromString("{% if (3 is divisable by 1) %}Hi{% else %}OH{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }
}
