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

package com.lyncode.jtwig.unit.content.model;

import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.model.compilable.SetVariable;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.render.RenderContext;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SetVariableTest {
    private final CompileContext compileContext = mock(CompileContext.class);
    private RenderContext renderContext = mock(RenderContext.class);
    private Expression expression = mock(Expression.class);
    private SetVariable underTest = new SetVariable("hello", toCalculate(expression));

    @Test
    public void setVariableShouldOnlyChangeTheContext() throws Exception {
        when(expression.calculate(renderContext)).thenReturn(null);

        underTest.compile(compileContext).render(renderContext);

        verify(renderContext, times(0)).write(any(byte[].class));
    }


    @Test(expected = RenderException.class)
    public void renderThrowsExceptionWhenCalculateException() throws Exception {
        when(expression.calculate(renderContext)).thenThrow(CalculateException.class);

        underTest.compile(compileContext).render(renderContext);
    }

    private CompilableExpression toCalculate (final Expression exp) {
        return new CompilableExpression() {
            @Override
            public Expression compile(CompileContext context) throws CompileException {
                return exp;
            }
        };
    }
}
