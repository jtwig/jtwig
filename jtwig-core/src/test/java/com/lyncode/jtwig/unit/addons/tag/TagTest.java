package com.lyncode.jtwig.unit.addons.tag;

import com.google.common.base.Function;
import com.lyncode.jtwig.addons.tag.Tag;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.model.compilable.Sequence;
import com.lyncode.jtwig.content.model.compilable.Text;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.render.RenderContext;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TagTest {
    private Function<String, String> transformation = mock(Function.class);
    private Tag underTest = new Tag(transformation)
            .withContent(new Sequence());
    private CompileContext compileContext = mock(CompileContext.class);
    private RenderContext renderContext = mock(RenderContext.class);

    @Before
    public void setUp() throws Exception {
        when(compileContext.hasParent()).thenReturn(false);
        when(compileContext.clone()).thenReturn(compileContext);
        when(compileContext.withParent(any(Sequence.class))).thenReturn(compileContext);
        when(renderContext.newRenderContext(any(OutputStream.class)))
                .thenReturn(renderContext);
    }

    @Test
    public void appliesTheTransformationToTheContent() throws Exception {
        given(transformation.apply(anyString())).willReturn("A");

        underTest
                .withContent(new Sequence().add(new Text("Hello")))
                .compile(compileContext)
                .render(renderContext);

        verify(transformation).apply("");
    }

    @Test(expected = RenderException.class)
    public void tagRenderWithIOException() throws Exception {
        when(transformation.apply(anyString()))
                .thenReturn("");
        doThrow(IOException.class)
                .when(renderContext)
                .write(any(byte[].class));

        underTest.compile(compileContext)
                .render(renderContext);
    }
}