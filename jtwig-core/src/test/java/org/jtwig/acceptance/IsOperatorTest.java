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
import org.jtwig.AbstractJtwigTest;
import org.junit.Test;

public class IsOperatorTest extends AbstractJtwigTest {
    public static final String HELLO = "one";
    
    @Test
    public void isOperatorEmptyFunction () throws Exception {
        withResource("{% if ([] is empty) %}Hi{% endif %}");
        assertThat(theResult(), is(equalTo("Hi")));
    }

    @Test
    public void isOperatorConstantFunction () throws Exception {
        withResource("{% if ('one' is constant('org.jtwig.acceptance.IsOperatorTest.HELLO')) %}Hi{% endif %}");
        assertThat(theResult(), is(equalTo("Hi")));
    }

    @Test
    public void isDefinedFunction () throws Exception {
        withResource("{% if (variable is not defined) %}Hi{% endif %}");
        assertThat(theResult(), is(equalTo("Hi")));
    }
    @Test
    public void isEvenFunction () throws Exception {
        withResource("{% if (1 is not even) %}Hi{% endif %}");
        assertThat(theResult(), is(equalTo("Hi")));
    }
    @Test
    public void isNotOddFunction () throws Exception {
        withResource("{% if (1 is not odd) %}Hi{% endif %}");
        assertThat(theResult(), is(equalTo("")));
    }
    @Test
    public void isOddFunction () throws Exception {
        withResource("{% if (1 is odd) %}Hi{% endif %}");
        assertThat(theResult(), is(equalTo("Hi")));
    }
    @Test
    public void isIterableFunction () throws Exception {
        withResource("{% if ([] is iterable) %}Hi{% endif %}");
        assertThat(theResult(), is(equalTo("Hi")));
    }
    @Test
    public void isIterableArrayFunction () throws Exception {
        model.add("value", new Object[0]);
        withResource("{% if (value is iterable) %}Hi{% endif %}");
        assertThat(theResult(), is(equalTo("Hi")));
    }
    @Test
    public void isIterableMapFunction () throws Exception {
        model.withModelAttribute("value", Collections.EMPTY_MAP);
        withResource("{% if (value is iterable) %}Hi{% endif %}");
        assertThat(theResult(), is(equalTo("Hi")));
    }

    @Test
    public void isNullFunction () throws Exception {
        model.withModelAttribute("value", null);
        withResource("{% if (value is null) %}Hi{% endif %}");
        assertThat(theResult(), is(equalTo("Hi")));
    }

    @Test
    public void isDivisableFunction () throws Exception {
        withResource("{% if (3 is divisable by 1) %}Hi{% else %}OH{% endif %}");
        assertThat(theResult(), is(equalTo("Hi")));
    }
}
