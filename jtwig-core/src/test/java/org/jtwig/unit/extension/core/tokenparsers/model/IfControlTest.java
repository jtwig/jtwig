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

package org.jtwig.unit.extension.core.tokenparsers.model;

import org.jtwig.Environment;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Compilable;
import org.jtwig.content.api.Renderable;
import org.jtwig.extension.core.tokenparsers.model.IfControl;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.loader.Loader;
import org.jtwig.render.RenderContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class IfControlTest {
    protected Environment env;
    protected Loader.Resource resource;
    
    @Before
    public void before() {
        env = new Environment();
        resource = mock(Loader.Resource.class);
    }
    
    @Test
    public void renderIfTrue() throws Exception {
        Renderable render = mock(Renderable.class);
        Expression condition = mock(Expression.class);
        IfControl control = new IfControl()
                .add(new IfControl.Case(toCalculate(condition)).withContent(toRender(render)));

        RenderContext context = mock(RenderContext.class);

        when(condition.calculate(context)).thenReturn(true);
        control.compile(new CompileContext(resource, env))
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

        when(condition.calculate(context)).thenReturn(false);
        control.compile(new CompileContext(resource, env))
                .render(context);

        verify(render, times(0)).render(context);
    }

    @Test
    public void tagWhiteSpace() throws Exception {
        IfControl.Case aCase = mock(IfControl.Case.class, Mockito.RETURNS_DEEP_STUBS);
        IfControl control = new IfControl().add(aCase);

        control.tag().whiteSpaceControl().trimAfterOpen();
        control.tag().whiteSpaceControl().trimBeforeClose();

        verify(aCase.tag().whiteSpaceControl()).trimAfterOpen();
        verify(aCase.tag().whiteSpaceControl()).trimBeforeClose();
    }


    @Test(expected = RenderException.class)
    public void renderThrowsExceptionWhenCalculateException() throws Exception {
        Renderable render = mock(Renderable.class);
        Expression condition = mock(Expression.class);
        IfControl control = new IfControl()
                .add(new IfControl.Case(toCalculate(condition)).withContent(toRender(render)));

        RenderContext context = mock(RenderContext.class);

        when(condition.calculate(context)).thenThrow(CalculateException.class);
        control.compile(new CompileContext(resource, env))
                .render(context);
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
