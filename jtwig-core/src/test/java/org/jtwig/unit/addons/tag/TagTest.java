package org.jtwig.unit.addons.tag;

import com.google.common.base.Function;
import java.io.IOException;
import java.io.OutputStream;
import org.jtwig.addons.tag.Tag;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.content.model.compilable.Text;
import org.jtwig.exception.RenderException;
import org.jtwig.render.RenderContext;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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