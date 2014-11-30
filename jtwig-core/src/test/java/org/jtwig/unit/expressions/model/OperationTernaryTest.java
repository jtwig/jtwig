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

import org.jtwig.compile.CompileContext;
import org.jtwig.exception.CompileException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.expressions.model.OperationTernary;
import org.jtwig.render.RenderContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class OperationTernaryTest {
    @Test
    public void testTrue() throws Exception {
        Expression expression = mock(Expression.class);
        Expression trueExp = mock(Expression.class);
        Expression falseExp = mock(Expression.class);
        when(expression.calculate(any(RenderContext.class))).thenReturn(true);
        when(trueExp.calculate(any(RenderContext.class))).thenReturn(1);

        RenderContext context = mock(RenderContext.class);
        Object result =
                new OperationTernary(null, expression(expression))
                        .withTrueExpression(expression(trueExp))
                        .withFalseExpression(expression(falseExp))
                        .compile(null)
                        .calculate(context);

        assertEquals(1, result);
        verify(falseExp, times(0)).calculate(any(RenderContext.class));
    }
    @Test
    public void testFalse() throws Exception {
        Expression expression = mock(Expression.class);
        Expression trueExp = mock(Expression.class);
        Expression falseExp = mock(Expression.class);
        when(expression.calculate(any(RenderContext.class))).thenReturn(false);
        when(falseExp.calculate(any(RenderContext.class))).thenReturn(2);

        RenderContext context = mock(RenderContext.class);
        Object result =
                new OperationTernary(null, expression(expression))
                        .withTrueExpression(expression(trueExp))
                        .withFalseExpression(expression(falseExp))
                        .compile(null)
                        .calculate(context);

        assertEquals(2, result);
        verify(trueExp, times(0)).calculate(any(RenderContext.class));
    }



    private CompilableExpression expression(final Expression expression) {
        return new CompilableExpression() {
            @Override
            public Expression compile(CompileContext context) throws CompileException {
                return expression;
            }
        };
    }
}
