package org.jtwig.unit.extension.core.tokenparsers;

import java.io.IOException;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.exception.RenderException;
import org.jtwig.extension.core.tokenparsers.ConcurrentTag;
import org.jtwig.render.RenderContext;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConcurrentTest {
    private final CompileContext compileContext = mock(CompileContext.class);
    private final RenderContext renderContext = mock(RenderContext.class);
    private ConcurrentTag.Concurrent concurrent = new ConcurrentTag.Concurrent()
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
