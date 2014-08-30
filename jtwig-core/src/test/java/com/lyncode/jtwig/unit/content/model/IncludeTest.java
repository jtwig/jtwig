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
import com.lyncode.jtwig.compile.config.CompileConfiguration;
import com.lyncode.jtwig.content.api.Compilable;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.content.model.compilable.Include;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class IncludeTest {
    private final RenderContext renderContext = mock(RenderContext.class);
    private CompileContext context;
    private JtwigResource jtwigResource;
    private JtwigParser jtwigParser;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp () {
        jtwigResource = mock(JtwigResource.class);
        jtwigParser = mock(JtwigParser.class);
        context = spy(new CompileContext(jtwigResource, jtwigParser, mock(CompileConfiguration.class)));
    }

    @Test
    public void includeShouldCompileRelativePathResource() throws Exception {
        Renderable renderable = mock(Renderable.class);

        Include include = new Include(null, "test");
        when(context.retrieve("test")).thenReturn(jtwigResource);
        when(context.clone()).thenReturn(context);
        when(context.parse(jtwigResource)).thenReturn(toRender(renderable));

        include.compile(context).render(renderContext);

        verify(renderable).render(renderContext);
    }


    @Test(expected = RenderException.class)
    public void renderWhenWithClauseCalculateException() throws Exception {
        Renderable renderable = mock(Renderable.class);
        Expression withExpression = mock(Expression.class);
        CompilableExpression expression = mock(CompilableExpression.class);

        Include include = new Include(null, "test").with(expression);

        when(expression.compile(context)).thenReturn(withExpression);
        when(context.retrieve("test")).thenReturn(jtwigResource);
        when(context.clone()).thenReturn(context);
        when(context.parse(jtwigResource)).thenReturn(toRender(renderable));
        when(withExpression.calculate(any(RenderContext.class))).thenThrow(CalculateException.class);

        doThrow(IOException.class)
                .when(renderContext)
                .write(any(byte[].class));

        include.compile(context)
                .render(renderContext);
    }

    @Test(expected = CompileException.class)
    public void compileWhenResourceException() throws Exception {
        Include include = new Include(null, "test");
        when(context.retrieve("test")).thenThrow(ResourceException.class);

        include.compile(context);
    }

    @Test
    public void withExpressionNotMap() throws Exception {
        Renderable renderable = mock(Renderable.class);
        Expression withExpression = mock(Expression.class);
        CompilableExpression expression = mock(CompilableExpression.class);

        Include include = new Include(null, "test").with(expression);

        when(expression.compile(context)).thenReturn(withExpression);
        when(context.retrieve("test")).thenReturn(jtwigResource);
        when(context.clone()).thenReturn(context);
        when(context.parse(jtwigResource)).thenReturn(toRender(renderable));
        when(withExpression.calculate(any(RenderContext.class))).thenReturn(new ArrayList<>());

        doThrow(IOException.class)
                .when(renderContext)
                .write(any(byte[].class));

        expectedException.expect(RenderException.class);
        expectedException.expectMessage(endsWith("Include 'with' must be given a map."));

        include.compile(context)
                .render(renderContext);
    }

    private Compilable toRender(final Renderable elementRender) {
        return new Compilable() {
            @Override
            public Renderable compile(CompileContext context) throws CompileException {
                return elementRender;
            }
        };
    }
}
