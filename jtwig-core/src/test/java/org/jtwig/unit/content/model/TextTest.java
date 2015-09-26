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

import java.io.IOException;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.content.model.compilable.Text;
import org.jtwig.exception.RenderException;
import org.jtwig.render.RenderContext;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TextTest {
    private Text underTest = new Text(" Hello ");
    private CompileContext compileContext = mock(CompileContext.class);
    private RenderContext renderContext = mock(RenderContext.class);

    @Before
    public void setUp() throws Exception {
        when(compileContext.clone()).thenReturn(compileContext);
        when(compileContext.withParent(any(Sequence.class))).thenReturn(compileContext);
    }

    @Test
    public void noChangesInTextWithoutSurroundingElements() throws Exception {
        underTest
            .compile(compileContext)
            .render(renderContext);

        verify(renderContext).write(" Hello ".getBytes());
    }

    @Test(expected = RenderException.class)
    public void renderWhenIOException() throws Exception {
        doThrow(IOException.class)
                .when(renderContext)
                .write(any(byte[].class));

        underTest.compile(compileContext)
                .render(renderContext);
    }

    private Renderable renderable(final String text) {
        return new Renderable() {
            @Override
            public void render(RenderContext context) throws RenderException {
                try {
                    context.write(text.getBytes());
                } catch (IOException e) {
                    throw new RenderException(e);
                }
            }
        };
    }
}
