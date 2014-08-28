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
import com.lyncode.jtwig.content.api.Compilable;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.content.model.compilable.For;
import com.lyncode.jtwig.content.model.compilable.Sequence;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.render.RenderContext;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

public class LoopControlTest {
    @Test
    public void forLoopTest() throws Exception {
        Renderable content = mock(Renderable.class);
        Expression expression = mock(Expression.class);
        RenderContext renderContext = mock(RenderContext.class);

        when(expression.calculate(any(RenderContext.class))).thenReturn(asList(1, 2));

        CompileContext compileContext = new CompileContext(null, null, null);

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
