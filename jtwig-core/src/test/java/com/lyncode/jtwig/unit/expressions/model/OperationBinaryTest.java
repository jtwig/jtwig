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
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.expressions.model.OperationBinary;
import com.lyncode.jtwig.expressions.model.Operator;
import com.lyncode.jtwig.expressions.model.Variable;
import com.lyncode.jtwig.render.RenderContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OperationBinaryTest {
    @Test
    public void addOperator() throws Exception {
        Expression left = mock(Expression.class);
        when(left.calculate(any(RenderContext.class))).thenReturn(1);
        Expression right = mock(Expression.class);
        when(right.calculate(any(RenderContext.class))).thenReturn(1);
        JtwigContext context = mock(JtwigContext.class);

        Object result = new OperationBinary(null, expression(left))
                .add(Operator.ADD)
                .add(expression(right))
                .compile(null)
                .calculate(RenderContext.create(null, context, null));

        assertEquals(2, result);
    }

    @Test
    public void compositionOperator() throws Exception {
        Expression left = mock(Expression.class);
        when(left.calculate(any(RenderContext.class))).thenReturn(1);
        Variable right = new Variable(null, "defined");
        JtwigContext context = new JtwigContext();

        Object result = new OperationBinary(null, expression(left))
                .add(Operator.COMPOSITION)
                .add(right)
                .compile(null)
                .calculate(RenderContext.create(null, context, null));

        assertEquals(true, result);
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
