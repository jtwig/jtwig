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
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.compilable.Include;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.exception.ResourceException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.render.RenderContext;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import org.jtwig.content.model.BasicTemplate;
import org.jtwig.content.model.Template;
import org.jtwig.expressions.model.Constant;
import org.jtwig.unit.AbstractUnitTest;

import static org.mockito.Mockito.*;

public class IncludeTest extends AbstractUnitTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void includeShouldCompileRelativePathResource() throws Exception {
        Renderable renderable = mock(Renderable.class);

        Include include = new Include(null, new Constant("test"));
        when(compileContext.retrieve("test")).thenReturn(resource);
        when(compileContext.clone()).thenReturn(compileContext);
        when(compileContext.parse(resource)).thenReturn(toTemplate(renderable));

        include.compile(compileContext).render(renderContext);

        verify(renderable).render(renderContext);
    }


    @Test(expected = RenderException.class)
    public void renderWhenWithClauseCalculateException() throws Exception {
        Renderable renderable = mock(Renderable.class);
        Expression withExpression = mock(Expression.class);
        CompilableExpression expression = mock(CompilableExpression.class);

        Include include = new Include(null, new Constant("test")).with(expression);

        when(expression.compile(compileContext)).thenReturn(withExpression);
        when(compileContext.retrieve("test")).thenReturn(resource);
        when(compileContext.clone()).thenReturn(compileContext);
        when(compileContext.parse(resource)).thenReturn(toTemplate(renderable));
        when(withExpression.calculate(any(RenderContext.class))).thenThrow(CalculateException.class);

        doThrow(IOException.class)
                .when(renderContext)
                .write(any(byte[].class));

        include.compile(compileContext)
                .render(renderContext);
    }

    @Test(expected = CompileException.class)
    public void compileWhenResourceException() throws Exception {
        Include include = new Include(null, new Constant("test"));
        when(compileContext.retrieve("test")).thenThrow(ResourceException.class);

        include.compile(compileContext);
    }

    @Test
    public void withExpressionNotMap() throws Exception {
        Renderable renderable = mock(Renderable.class);
        Expression withExpression = mock(Expression.class);
        CompilableExpression expression = mock(CompilableExpression.class);

        Include include = new Include(null, new Constant("test")).with(expression);

        when(expression.compile(compileContext)).thenReturn(withExpression);
        when(compileContext.retrieve("test")).thenReturn(resource);
        when(compileContext.clone()).thenReturn(compileContext);
        when(compileContext.parse(resource)).thenReturn(toTemplate(renderable));
        when(withExpression.calculate(any(RenderContext.class))).thenReturn(new ArrayList<>());

        doThrow(IOException.class)
                .when(renderContext)
                .write(any(byte[].class));

        expectedException.expect(RenderException.class);
        expectedException.expectMessage(endsWith("Include 'with' must be given a map."));

        include.compile(compileContext)
                .render(renderContext);
    }

    private Template toTemplate(final Renderable elementRender) {
        return new BasicTemplate(null) {
            @Override
            public CompiledBasicTemplate compile(CompileContext context) throws CompileException {
                return new CompiledBasicTemplate(null, null, null, elementRender);
            }
        };
    }
}
