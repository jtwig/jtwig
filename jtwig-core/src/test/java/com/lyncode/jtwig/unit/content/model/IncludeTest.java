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
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class IncludeTest {
    private CompileContext context;
    private JtwigResource jtwigResource;
    private JtwigParser jtwigParser;

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

        RenderContext renderContext = mock(RenderContext.class);
        include.compile(context).render(renderContext);

        verify(renderable).render(renderContext);
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
