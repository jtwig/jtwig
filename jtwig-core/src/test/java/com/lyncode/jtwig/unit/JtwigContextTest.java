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

package com.lyncode.jtwig.unit;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.expressions.model.Variable;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JtwigContextTest {
    private JtwigModelMap modelMap = mock(JtwigModelMap.class);
    private JtwigPosition position = mock(JtwigPosition.class);
    private JtwigContext context = new JtwigContext(modelMap);

    @Test
    public void shouldResolveVariable() throws Exception {
        addItem("name", "joao");
        assertThat(new Variable(position, "name").compile(null).calculate(RenderContext.create(null, context, null)), is((Object) "joao"));
    }

    private void addItem(String key, Object value) {
        when(modelMap.get(key)).thenReturn(value);
        when(modelMap.containsKey(key)).thenReturn(true);
    }
}
