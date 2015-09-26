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

import org.jtwig.unit.AbstractJtwigTest;
import org.jtwig.compile.CompileContext;
import org.jtwig.exception.CompileException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.expressions.model.OperationBinary;
import org.jtwig.extension.model.FilterCall;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OperationBinaryTest extends AbstractJtwigTest {
    @Test
    public void addOperator() throws Exception {
        Expression left = mock(Expression.class);
        when(left.calculate(any(RenderContext.class))).thenReturn(1);
        Expression right = mock(Expression.class);
        when(right.calculate(any(RenderContext.class))).thenReturn(1);

        Object result = new OperationBinary(null, expression(left))
                .addOperator("+")
                .addOperand(expression(right))
                .compile(compileContext)
                .calculate(renderContext);

        assertEquals(2L, result);
    }

    @Test
    public void compositionOperator() throws Exception {
        Expression left = mock(Expression.class);
        when(left.calculate(any(RenderContext.class))).thenReturn(1);
        FilterCall filter = new FilterCall(null, "split");

        Object result = new OperationBinary(null, expression(left))
                .addOperator("|")
                .addOperand(filter)
                .compile(compileContext)
                .calculate(renderContext);

        assertArrayEquals(new Character[]{'1'}, (Character[])result);
    }

    @Test(expected = CompileException.class)
    public void unknownOperation() throws Exception {
        new OperationBinary(new JtwigPosition(null, 1, 1), mock(CompilableExpression.class))
                .addOperator("unknown")
                .addOperand(mock(CompilableExpression.class)).compile(compileContext);
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
