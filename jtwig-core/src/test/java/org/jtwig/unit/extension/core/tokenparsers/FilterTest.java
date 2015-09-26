package org.jtwig.unit.extension.core.tokenparsers;

import java.io.IOException;
import java.io.OutputStream;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.exception.RenderException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.extension.core.tokenparsers.FilterTag;
import org.jtwig.extension.model.FilterCall;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FilterTest {
    private final CompilableExpression expression = mock(CompilableExpression.class);
    private final CompileContext compileContext = mock(CompileContext.class);
    private final RenderContext renderContext = mock(RenderContext.class);
    private final JtwigPosition position = mock(JtwigPosition.class);
    private final FilterCall.Compiled filterCall = mock(FilterCall.Compiled.class);
    private FilterTag.Filter underTest = new FilterTag.Filter(position)
            .withFilterExpression(expression)
            .withContent(new Sequence());

    @Before
    public void setUp() throws Exception {
        when(compileContext.clone()).thenReturn(compileContext);
        when(filterCall.calculate(renderContext)).thenReturn("test");
        when(expression.compile(compileContext)).thenReturn(filterCall);
        when(renderContext.newRenderContext(any(OutputStream.class))).thenReturn(renderContext);
        when(filterCall.passLeft(any(Expression.class))).thenReturn(filterCall);
    }

    @Test(expected = RenderException.class)
    public void filterRenderWithIOException() throws Exception {
        doThrow(IOException.class)
                .when(renderContext)
                .write(any(byte[].class));

        underTest.compile(compileContext)
                .render(renderContext);
    }
}
