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

package org.jtwig.unit.content.model;

import org.jtwig.compile.CompileContext;
import org.jtwig.content.model.compilable.Output;
import org.jtwig.exception.CompileException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.render.RenderContext;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class OutputTest {
    private Expression expression = mock(Expression.class);
    private Output output = new Output(toCalculable(expression));

    @Test
    public void checkOutput() throws Exception {
        CompileContext context = mock(CompileContext.class);
        RenderContext renderContext = mock(RenderContext.class);

        output.compile(context).render(renderContext);

        verify(expression).calculate(any(RenderContext.class));
    }

    private CompilableExpression toCalculable (final Expression exp) {
        return new CompilableExpression() {
            @Override
            public Expression compile(CompileContext context) throws CompileException {
                return exp;
            }
        };
    }
}
