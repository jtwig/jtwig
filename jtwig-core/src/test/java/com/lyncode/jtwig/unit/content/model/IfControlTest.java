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

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.compile.config.CompileConfiguration;
import com.lyncode.jtwig.content.api.Compilable;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.content.model.compilable.IfControl;
import com.lyncode.jtwig.content.model.compilable.Sequence;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class IfControlTest {
    @Test
    public void renderIfTrue() throws Exception {
        Renderable render = mock(Renderable.class);
        Expression condition = mock(Expression.class);
        IfControl control = new IfControl()
                .add(new IfControl.Case(toCalculate(condition)).withContent(toRender(render)));

        RenderContext context = mock(RenderContext.class);
        JtwigContext jtwigContext = new JtwigContext();
        when(context.model()).thenReturn(jtwigContext);

        when(condition.calculate(context)).thenReturn(true);
        control.compile(new CompileContext(mock(JtwigResource.class), mock(JtwigParser.class), mock(CompileConfiguration.class)))
                .render(context);

        verify(render).render(context);
    }
    @Test
    public void doNotRenderIfFalse() throws Exception {
        Renderable render = mock(Renderable.class);
        Expression condition = mock(Expression.class);
        IfControl control = new IfControl()
                .add(new IfControl.Case(toCalculate(condition)).withContent(toRender(render)));

        RenderContext context = mock(RenderContext.class);
        JtwigContext jtwigContext = new JtwigContext();
        when(context.model()).thenReturn(jtwigContext);

        when(condition.calculate(context)).thenReturn(false);
        control.compile(new CompileContext(mock(JtwigResource.class), mock(JtwigParser.class), mock(CompileConfiguration.class)))
                .render(context);

        verify(render, times(0)).render(context);
    }




    private Sequence toRender(final Renderable elementRender) {
        return new Sequence().add(new Compilable() {
            @Override
            public Renderable compile(CompileContext context) throws CompileException {
                return elementRender;
            }
        });
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
