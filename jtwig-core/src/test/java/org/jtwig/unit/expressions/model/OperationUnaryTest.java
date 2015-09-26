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
import org.jtwig.expressions.model.OperationUnary;
import org.jtwig.render.RenderContext;
import org.jtwig.unit.AbstractJtwigTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OperationUnaryTest extends AbstractJtwigTest {
    @Test
    public void not() throws Exception {
        Expression expression = mock(Expression.class);
        when(expression.calculate(any(RenderContext.class))).thenReturn(true);

        OperationUnary unary = new OperationUnary(null, "not").withOperand(expression(expression));
        assertEquals(unary.compile(compileContext).calculate(renderContext), false);
    }

    @Test(expected = CompileException.class)
    public void operationNotFound() throws Exception {
        new OperationUnary(null, "unknown").withOperand(mock(CompilableExpression.class)).compile(compileContext);
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
