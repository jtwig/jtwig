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
import com.lyncode.jtwig.content.model.compilable.Block;
import com.lyncode.jtwig.content.model.compilable.Extends;
import com.lyncode.jtwig.content.model.compilable.Sequence;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class CompileExtendsDocumentTest {
    private JtwigParser parser;
    private JtwigResource resource;

    @Before
    public void setup () {
        parser = mock(JtwigParser.class);
        resource = mock(JtwigResource.class);
    }

    @Test
    public void blockShouldBeReplaced() throws Exception {
        Renderable elementRender = mock(Renderable.class);
        Renderable replaceRender = mock(Renderable.class);
        JtwigResource extendTemplate = mock(JtwigResource.class);

        when(resource.resolve("abc")).thenReturn(extendTemplate);
        when(parser.parse(extendTemplate)).thenReturn(
                new Sequence().add(new Block("block").withContent(toRender(elementRender)))
        );

        CompileContext context = new CompileContext(resource, parser, null);

        Extends extendsDocument = new Extends("abc")
                .add(new Block("block").withContent(toRender(replaceRender)));


        Renderable compiled = extendsDocument.compile(context);

        RenderContext renderContext = mock(RenderContext.class);
        compiled.render(renderContext);

        verify(replaceRender, times(1)).render(any(RenderContext.class));
        verify(elementRender, times(0)).render(any(RenderContext.class));
    }


    @Test
    public void blockShouldBeReplacedInNestedBlocks() throws Exception {
        Renderable elementRender = mock(Renderable.class);
        Renderable replaceRender = mock(Renderable.class);

        JtwigResource extendTemplate = mock(JtwigResource.class);
        when(resource.resolve("abc")).thenReturn(extendTemplate);
        when(parser.parse(extendTemplate)).thenReturn(
                new Sequence().add(new Block("asd").withContent(new Sequence().add(new Block("block").withContent(toRender(elementRender)))))
        );

        CompileContext context = new CompileContext(resource, parser, null);

        Extends extendsDocument = new Extends("abc")
                .add(new Block("block").withContent(toRender(replaceRender)));


        Renderable compiled = extendsDocument.compile(context);

        RenderContext renderContext = mock(RenderContext.class);
        compiled.render(renderContext);

        verify(replaceRender, times(1)).render(any(RenderContext.class));
        verify(elementRender, times(0)).render(any(RenderContext.class));
    }


    @Test
    public void nestedExtend() throws Exception {
        Renderable blockA = mock(Renderable.class);
        Renderable blockB = mock(Renderable.class);
        Renderable blockC = mock(Renderable.class);

        Extends extendsDocument = new Extends("user")
                .add(new Block("a").withContent(toRender(blockC)));


        JtwigResource body = mock(JtwigResource.class);
        when(resource.resolve("user")).thenReturn(body);
        when(parser.parse(body)).thenReturn(
                new Extends("layout")
                    .add(new Block("a").withContent(toRender(blockB)))
        );
        JtwigResource layout = mock(JtwigResource.class);
        when(body.resolve("layout")).thenReturn(layout);
        when(parser.parse(layout)).thenReturn(
                new Sequence().add(new Block("a").withContent(toRender(blockA)))
        );

        CompileContext context = new CompileContext(resource, parser, null);



        Renderable compiled = extendsDocument.compile(context);

        RenderContext renderContext = mock(RenderContext.class);
        compiled.render(renderContext);

        verify(blockA, times(0)).render(any(RenderContext.class));
        verify(blockB, times(0)).render(any(RenderContext.class));
        verify(blockC, times(1)).render(any(RenderContext.class));
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
