package com.lyncode.jtwig.unit.addons.concurrent;

import com.lyncode.jtwig.addons.concurrent.Concurrent;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.content.model.compilable.Sequence;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.render.RenderContext;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ConcurrentTest {
    private final CompileContext compileContext = mock(CompileContext.class);
    private final RenderContext renderContext = mock(RenderContext.class);
    private Concurrent concurrent = new Concurrent()
            .withContent(new Sequence());

    @Before
    public void setUp() throws Exception {
        when(compileContext.clone()).thenReturn(compileContext);
    }

    @Test(expected = RenderException.class)
    public void renderThrowsExceptionWhenIOException() throws Exception {
        doThrow(IOException.class)
                .when(renderContext)
                .renderConcurrent(any(Renderable.class));

        concurrent.compile(compileContext).render(renderContext);
    }
}