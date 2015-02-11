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
import org.jtwig.AbstractJtwigTest;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.compilable.Content;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.content.model.compilable.Text;
import org.jtwig.content.model.tag.TagInformation;
import org.jtwig.exception.RenderException;
import org.jtwig.render.RenderContext;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TextTest extends AbstractJtwigTest {
    private Text underTest = new Text(" Hello ");

    @Test
    public void noChangesInTextWithoutSurroundingElements() throws Exception {
        underTest.compile(compileContext).render(renderContext);

        verify(renderContext).write(" Hello ".getBytes());
    }

    @Test
    public void removingStartingWhiteSpaces() throws Exception {
        Content before = mock(Content.class);
        when(before.compile(any(CompileContext.class))).thenReturn(renderable(""));
        TagInformation tagInformation = new TagInformation();
        tagInformation.whiteSpaceControl().trimAfterEnd(true);
        when(before.tag()).thenReturn(tagInformation);

        new Sequence()
                .add(before)
                .add(underTest)
                .compile(compileContext)
                .render(renderContext);

        verify(renderContext).write("Hello ".getBytes());
    }

    @Test
    public void removingEndingWhiteSpaces() throws Exception {
        Content after = mock(Content.class);
        when(after.compile(any(CompileContext.class))).thenReturn(renderable(""));
        TagInformation tagInformation = new TagInformation();
        tagInformation.whiteSpaceControl().trimBeforeBegin(true);
        when(after.tag()).thenReturn(tagInformation);

        new Sequence()
                .add(underTest)
                .add(after)
                .compile(compileContext)
                .render(renderContext);

        verify(renderContext).write(" Hello".getBytes());
    }

    @Test(expected = RenderException.class)
    public void renderWhenIOException() throws Exception {
        doThrow(IOException.class)
                .when(renderContext)
                .write(any(byte[].class));

        underTest.compile(compileContext)
                .render(renderContext);
    }

    @Test
    public void builderIsFakeContent() throws Exception {
        Text.Builder b = new Text.Builder();
        assertNull(b.compile(null));
    }

    @Test
    public void removingBothSidesWhiteSpaces() throws Exception {
        Content before = mock(Content.class);
        when(before.compile(any(CompileContext.class))).thenReturn(renderable(""));
        TagInformation beforeTagInformation = new TagInformation();
        beforeTagInformation.whiteSpaceControl().trimAfterEnd(true);
        when(before.tag()).thenReturn(beforeTagInformation);

        Content after = mock(Content.class);
        when(after.compile(any(CompileContext.class))).thenReturn(renderable(""));
        TagInformation afterTagInformation = new TagInformation();
        afterTagInformation.whiteSpaceControl().trimBeforeBegin(true);
        when(after.tag()).thenReturn(afterTagInformation);



        new Sequence()
                .add(before)
                .add(underTest)
                .add(after)
                .compile(compileContext)
                .render(renderContext);

        verify(renderContext).write("Hello".getBytes());
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
