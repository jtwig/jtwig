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
package org.jtwig.acceptance.extension.core.functions;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.not;
import org.jtwig.JtwigModelMap;
import static org.jtwig.JtwigTemplate.inlineTemplate;
import org.jtwig.exception.RenderException;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class RandomFunctionTest {
    @Test
    public void generalTests() throws Exception {
        assertThat(
                inlineTemplate("{{ random(['apple','orange','banana']) }}").render(),
                isOneOf("apple", "orange", "banana"));
        assertThat(
                inlineTemplate("{{ random({'a':'apple','o':'orange','b':'banana'}) }}").render(),
                isOneOf("apple", "orange", "banana"));
        assertThat(
                inlineTemplate("{{ random('abc') }}").render(),
                isOneOf("a", "b", "c"));
        assertThat(
                inlineTemplate("{{ random() }}").render(),
                not(isEmptyString()));
        assertThat(
                inlineTemplate("{{ random(5) }}").render(),
                isOneOf("0","1","2","3","4"));
        assertThat(
                inlineTemplate("{{ random(null) }}").render(),
                is(equalTo("")));
        assertThat(
                inlineTemplate("{{ random(arr) }}").render(new JtwigModelMap().add("arr", (Object)Collections.singleton("a"))),
                is(equalTo("a")));
        assertThat(
                inlineTemplate("{{ random(set) }}").render(new JtwigModelMap()
                        .add("set", new LinkedHashSet<>(Arrays.asList("apple", "orange", "banana")))),
                isOneOf("apple", "banana", "orange"));
    }
    
    @Test(expected = RenderException.class)
    public void testAgainstEmptyArray() throws Exception {
        inlineTemplate("{{ random([]) }}").render();
    }
}