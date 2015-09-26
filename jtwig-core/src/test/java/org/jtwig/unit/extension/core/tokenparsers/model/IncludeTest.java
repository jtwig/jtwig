/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jtwig.unit.extension.core.tokenparsers.model;

import java.io.IOException;
import java.util.ArrayList;
import org.jtwig.unit.AbstractJtwigTest;
import org.jtwig.Environment;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.Template;
import org.jtwig.extension.core.tokenparsers.model.Include;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.exception.ResourceException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.expressions.model.Constant;
import org.jtwig.loader.Loader;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.endsWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.internal.stubbing.answers.ReturnsArgumentAt;

public class IncludeTest extends AbstractJtwigTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private JtwigPosition position;
    private Renderable renderable;
    
    @Before
    @Override
    public void before() throws Exception {
        super.before();
        when(resource.getCacheKey()).thenReturn("_parent_");
        when(resource.resolve(any(String.class))).then(new ReturnsArgumentAt(0));
        position = new JtwigPosition(resource, 0, 0);
        
        renderable = mock(Renderable.class);
        Loader.Resource test = mock(Loader.Resource.class);
        when(theEnvironment().load("test")).thenReturn(test);
        Mockito.doReturn(toTemplate(renderable))
                .when(theEnvironment())
                .parse(test);
        
        when(compileContext.clone()).thenReturn(compileContext);
    }
    @Override
    protected Environment buildEnvironment() {
        return spy(new Environment());
    }

    @Test
    public void includeShouldCompileRelativePathResource() throws Exception {
        Include include = new Include(position, new Constant("test"));
        include.compile(compileContext).render(renderContext);
        verify(renderable).render(renderContext);
    }

    @Test(expected = RenderException.class)
    public void renderWhenWithClauseCalculateException() throws Exception {
        Expression withExpression = mock(Expression.class);
        when(withExpression.calculate(any(RenderContext.class))).thenThrow(CalculateException.class);
        
        CompilableExpression expression = mock(CompilableExpression.class);
        when(expression.compile(compileContext)).thenReturn(withExpression);

        Include include = new Include(position, new Constant("test")).with(expression);

        doThrow(IOException.class)
                .when(renderContext)
                .write(any(byte[].class));

        include.compile(compileContext)
                .render(renderContext);
    }

    @Test(expected = CompileException.class)
    public void compileWhenResourceException() throws Exception {
        Include include = new Include(position, new Constant("test"));
        when(theEnvironment().load("test")).thenThrow(ResourceException.class);

        include.compile(compileContext);
    }

    @Test
    public void withExpressionNotMap() throws Exception {
        Expression withExpression = mock(Expression.class);
        CompilableExpression expression = mock(CompilableExpression.class);

        Include include = new Include(position, new Constant("test")).with(expression);

        when(expression.compile(compileContext)).thenReturn(withExpression);
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
        return new Template(position) {
            @Override
            public Compiled compile(CompileContext context) throws CompileException {
                return new Compiled(position, null, null, null, elementRender, context);
            }
        };
    }
}
