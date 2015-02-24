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

import static java.util.Arrays.asList;
import org.jtwig.unit.AbstractJtwigTest;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Compilable;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.compilable.For;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.exception.CompileException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.render.RenderContext;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoopControlTest extends AbstractJtwigTest {
    @Test
    public void forLoopTest() throws Exception {
        Renderable content = mock(Renderable.class);
        Expression expression = mock(Expression.class);
        when(expression.calculate(any(RenderContext.class))).thenReturn(asList(1, 2));

        For loopControl = new For("variable", toCalculate(expression)).withContent(toRender(content));
        loopControl.compile(compileContext).render(renderContext);

        verify(content, times(2)).render(renderContext);
    }

    private CompilableExpression toCalculate (final Expression exp) {
        return new CompilableExpression() {
            @Override
            public Expression compile(CompileContext context) throws CompileException {
                return exp;
            }
        };
    }

    private Sequence toRender(final Renderable elementRender) {
        return new Sequence().add(new Compilable() {
            @Override
            public Renderable compile(CompileContext context) throws CompileException {
                return elementRender;
            }
        });
    }
}
