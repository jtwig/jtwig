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

package org.jtwig.unit.expressions.model;

import org.jtwig.JtwigModelMap;
import org.jtwig.compile.CompileContext;
import org.jtwig.expressions.api.Expression;
import org.jtwig.expressions.model.Constant;
import org.jtwig.expressions.model.ValueMap;
import org.jtwig.render.RenderContext;
import org.jtwig.render.config.RenderConfiguration;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ValueMapTest {
    @Test
    public void test() throws Exception {
        ValueMap underTest = new ValueMap(null)
                .add("key", new Constant<>(null));

        CompileContext context = mock(CompileContext.class);

        Expression expression = underTest.compile(context);
        assertNotNull(expression);

        Map map = (Map) expression.calculate(RenderContext.create(new RenderConfiguration(), new JtwigModelMap(), null));
        assertTrue(map.containsKey("key"));
    }
}
