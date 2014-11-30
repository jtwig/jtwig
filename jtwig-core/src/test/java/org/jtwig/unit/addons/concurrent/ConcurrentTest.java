package org.jtwig.unit.addons.concurrent;

import org.jtwig.addons.concurrent.Concurrent;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.exception.RenderException;
import org.jtwig.render.RenderContext;
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
