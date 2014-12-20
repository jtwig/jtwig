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
import org.jtwig.cache.impl.ExecutionCache;
import org.jtwig.compile.CompileContext;
import org.jtwig.exception.CompileException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.expressions.model.OperationBinary;
import org.jtwig.expressions.model.Operator;
import org.jtwig.expressions.model.Variable;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.jtwig.render.config.RenderConfiguration;
import org.junit.Test;

import static org.jtwig.expressions.model.Operator.UNKNOWN;
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
        JtwigModelMap context = mock(JtwigModelMap.class);

        Object result = new OperationBinary(null, expression(left))
                .add(Operator.ADD)
                .add(expression(right))
                .compile(null)
                .calculate(RenderContext.create(new RenderConfiguration(new ExecutionCache()), context, null));

        assertEquals(2, result);
    }

    @Test
    public void compositionOperator() throws Exception {
        Expression left = mock(Expression.class);
        when(left.calculate(any(RenderContext.class))).thenReturn(1);
        Variable right = new Variable(null, "defined");
        JtwigModelMap context = new JtwigModelMap();

        Object result = new OperationBinary(null, expression(left))
                .add(Operator.COMPOSITION)
                .add(right)
                .compile(null)
                .calculate(RenderContext.create(new RenderConfiguration(new ExecutionCache()), context, null));

        assertEquals(true, result);
    }

    @Test(expected = CompileException.class)
    public void unknownOperation() throws Exception {
        new OperationBinary(new JtwigPosition(null, 1, 1), mock(CompilableExpression.class))
                .add(UNKNOWN)
                .add(mock(CompilableExpression.class)).compile(mock(CompileContext.class));
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
