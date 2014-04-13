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

package com.lyncode.jtwig.acceptance;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.JtwigTemplate;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class IsOperatorTest extends AbstractJtwigTest {
    public static final String HELLO = "one";
    private JtwigContext context = new JtwigContext();

    @Test
    public void isOperatorEmptyFunction () throws Exception {
        JtwigTemplate template = new JtwigTemplate("{% if ([] is empty) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }

    @Test
    public void isOperatorConstantFunction () throws Exception {
        JtwigTemplate template = new JtwigTemplate("{% if ('one' is constant('com.lyncode.jtwig.acceptance.IsOperatorTest.HELLO')) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }

    @Test
    public void isDefinedFunction () throws Exception {
        JtwigTemplate template = new JtwigTemplate("{% if (variable is not defined) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }
    @Test
    public void isEvenFunction () throws Exception {
        JtwigTemplate template = new JtwigTemplate("{% if (1 is not even) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }
    @Test
    public void isOddFunction () throws Exception {
        JtwigTemplate template = new JtwigTemplate("{% if (1 is odd) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }
    @Test
    public void isIterableFunction () throws Exception {
        JtwigTemplate template = new JtwigTemplate("{% if ([] is iterable) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }
    @Test
    public void isIterableArrayFunction () throws Exception {
        context.withModelAttribute("value", new Object[0]);
        JtwigTemplate template = new JtwigTemplate("{% if (value is iterable) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }
    @Test
    public void isIterableMapFunction () throws Exception {
        context.withModelAttribute("value", new HashMap<>());
        JtwigTemplate template = new JtwigTemplate("{% if (value is iterable) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }

    @Test
    public void isNullFunction () throws Exception {
        context.withModelAttribute("value", null);
        JtwigTemplate template = new JtwigTemplate("{% if (value is null) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }

    @Test
    public void isDivisableFunction () throws Exception {
        JtwigTemplate template = new JtwigTemplate("{% if (3 is divisable by 1) %}Hi{% endif %}");
        assertThat(template.output(context), is(equalTo("Hi")));
    }
}
