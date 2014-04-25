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

package com.lyncode.jtwig.unit.expressions.model;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.expressions.model.Constant;
import com.lyncode.jtwig.expressions.model.MapSelection;
import com.lyncode.jtwig.expressions.model.Variable;
import com.lyncode.jtwig.render.RenderContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static com.lyncode.jtwig.types.Undefined.UNDEFINED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MapSelectionTest {
    @Test
    public void selection() throws Exception {
        Variable variable = mock(Variable.class);
        CompilableExpression key = new Constant<>("test");
        CompileContext context = mock(CompileContext.class);
        JtwigContext jtwigContext = mock(JtwigContext.class);

        when(variable.compile(context)).thenReturn(mapExpression("test", "value"));

        MapSelection selection = new MapSelection(null, variable, key);
        Expression compiled = selection.compile(context);

        assertThat(compiled, notNullValue(Expression.class));
        assertEquals("value", compiled.calculate(RenderContext.create(null, jtwigContext, null)));
    }

    @Test(expected = CalculateException.class)
    public void undefinedKey() throws Exception {
        Variable variable = mock(Variable.class);
        CompilableExpression key = new Constant<>(UNDEFINED);
        CompileContext context = mock(CompileContext.class);
        JtwigContext jtwigContext = mock(JtwigContext.class);

        when(variable.compile(context)).thenReturn(mapExpression("test", "value"));

        MapSelection selection = new MapSelection(null, variable, key);
        Expression compiled = selection.compile(context);

        assertThat(compiled, notNullValue(Expression.class));
        compiled.calculate(RenderContext.create(null, jtwigContext, null));
    }


    @Test(expected = CalculateException.class)
    public void nonMapVariable() throws Exception {
        Variable variable = mock(Variable.class);
        CompilableExpression key = new Constant<>("test");
        CompileContext context = mock(CompileContext.class);
        JtwigContext jtwigContext = mock(JtwigContext.class);

        when(variable.compile(context)).thenReturn(new Expression() {
            @Override
            public Object calculate(RenderContext context) throws CalculateException {
                return new ArrayList<>();
            }
        });

        MapSelection selection = new MapSelection(null, variable, key);
        Expression compiled = selection.compile(context);

        assertThat(compiled, notNullValue(Expression.class));
        compiled.calculate(RenderContext.create(null, jtwigContext, null));
    }

    private Expression mapExpression(final String key, final String value) {
        return new Expression() {
            @Override
            public Object calculate(RenderContext context) throws CalculateException {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(key, value);
                return map;
            }
        };
    }
}
