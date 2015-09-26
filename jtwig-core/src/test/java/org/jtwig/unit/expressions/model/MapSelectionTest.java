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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import org.jtwig.Environment;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.configuration.JtwigConfigurationBuilder;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.expressions.model.Constant;
import org.jtwig.expressions.model.MapSelection;
import org.jtwig.expressions.model.Variable;
import org.jtwig.render.RenderContext;
import org.jtwig.types.Undefined;
import static org.jtwig.types.Undefined.UNDEFINED;
import org.jtwig.unit.AbstractJtwigTest;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MapSelectionTest extends AbstractJtwigTest {
    @Test
    public void selection() throws Exception {
        Variable variable = variable(Collections.singletonMap("test", "value"));
        CompilableExpression key = new Constant<>("test");

        MapSelection selection = new MapSelection(null, variable, key);
        Expression compiled = selection.compile(compileContext);

        assertThat(compiled, notNullValue(Expression.class));
        assertEquals("value", compiled.calculate(renderContext));
    }

    @Test(expected = CalculateException.class)
    public void undefinedKey() throws Exception {
        Variable variable = variable(Collections.singletonMap("test", "value"));
        CompilableExpression key = new Constant<>(UNDEFINED);

        MapSelection selection = new MapSelection(null, variable, key);
        Expression compiled = selection.compile(compileContext);

        assertThat(compiled, notNullValue(Expression.class));
        compiled.calculate(renderContext);
    }
    
    @Test
    public void undefinedVariableWithoutStrictMode() throws Exception {
        CompilableExpression key = new Constant<>("test");

        MapSelection selection = new MapSelection(null, new Variable(null, "missing"), key);
        Expression compiled = selection.compile(compileContext);

        assertThat(compiled, notNullValue(Expression.class));
        compiled.calculate(renderContext);
    }
    
    @Test(expected = CalculateException.class)
    public void undefinedVariableWithStrictMode() throws Exception {
        CompilableExpression key = new Constant<>(UNDEFINED);

        MapSelection selection = new MapSelection(null, new Variable(null, "missing"), key);
        Expression compiled = selection.compile(compileContext);

        assertThat(compiled, notNullValue(Expression.class));
        compiled.calculate(renderContext);
    }
    
    @Test
    public void testUndefinedMapEntry() throws Exception {
        Variable variable = variable(Collections.singletonMap("test", "value"));
        CompilableExpression key = new Constant<>("unknown");

        MapSelection selection = new MapSelection(null, variable, key);
        Expression compiled = selection.compile(compileContext);

        assertThat(compiled, notNullValue(Expression.class));
        assertEquals(UNDEFINED, compiled.calculate(renderContext));
    }
    
    @Test
    public void testSetEntry() throws Exception {
        Variable variable = variable(new LinkedHashSet(Arrays.asList("test", "test2")));

        MapSelection selection = new MapSelection(null, variable, new Constant<>("0"));
        Expression compiled = selection.compile(compileContext);
        assertEquals("test", compiled.calculate(renderContext));
        
        selection = new MapSelection(null, variable, new Constant<>("5"));
        compiled = selection.compile(compileContext);
        assertEquals(UNDEFINED, compiled.calculate(renderContext));
    }
    
    @Test
    public void testAgainstObject() throws Exception {
        Variable variable = variable(new Object());

        MapSelection selection = new MapSelection(null, variable, new Constant<>("0"));
        Expression compiled = selection.compile(compileContext);
        assertEquals(UNDEFINED, compiled.calculate(renderContext));
    }
    @Test(expected = CalculateException.class)
    public void testAgainstObjectWithStrictMode() throws Exception {
        JtwigConfiguration config = JtwigConfigurationBuilder.newConfiguration()
                .withStrictMode(true)
                .build();
        this.env = new Environment(config);
        buildContexts();
        Variable variable = variable(new Object());

        MapSelection selection = new MapSelection(null, variable, new Constant<>("0"));
        selection.compile(compileContext).calculate(renderContext);
    }
    @Test
    public void testAgainstObjectWithLogging() throws Exception {
        JtwigConfiguration config = JtwigConfigurationBuilder.newConfiguration()
                .withLogNonStrictMode(true)
                .build();
        this.env = new Environment(config);
        buildContexts();
        Variable variable = variable(new Object());

        MapSelection selection = new MapSelection(null, variable, new Constant<>("0"));
        Expression compiled = selection.compile(compileContext);
        assertEquals(UNDEFINED, compiled.calculate(renderContext));
    }
    
    private Variable variable(final Object returnValue) throws CompileException {
        Variable variable = mock(Variable.class);
        when(variable.compile(compileContext)).thenReturn(new Expression() {
            @Override
            public Object calculate(RenderContext context) throws CalculateException {
                return returnValue;
            }
        });
        return variable;
    }
}
