package org.jtwig.unit.content.model.compilable;

import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.compilable.Extends;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ResourceException;
import org.jtwig.render.RenderContext;
import org.jtwig.resource.JtwigResource;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class ExtendsTest {
    private final RenderContext renderContext = mock(RenderContext.class);
    private final CompileContext compileContext = mock(CompileContext.class);
    private Extends underTest = new Extends("test");

    @Before
    public void setUp() throws Exception {
        when(compileContext.clone()).thenReturn(compileContext);
        when(compileContext.parse(any(JtwigResource.class)))
                .thenThrow(ResourceException.class);
        when(compileContext.withResource(any(JtwigResource.class)))
                .thenReturn(compileContext);
        when(compileContext.withReplacement(anyString(), any(Renderable.class)))
                .thenReturn(compileContext);
    }

    @Test(expected = CompileException.class)
    public void renderThrowsExceptionWhenIOException() throws Exception {
        doThrow(IOException.class)
                .when(renderContext)
                .renderConcurrent(any(Renderable.class));

        underTest.compile(compileContext);
    }
}
