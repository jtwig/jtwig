package org.jtwig.unit.addons.filter;

import org.jtwig.addons.filter.Filter;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.exception.RenderException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.expressions.model.FunctionElement;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class FilterTest {
    private final CompilableExpression expression = mock(CompilableExpression.class);
    private final CompileContext compileContext = mock(CompileContext.class);
    private final RenderContext renderContext = mock(RenderContext.class);
    private final JtwigPosition position = mock(JtwigPosition.class);
    private final FunctionElement.Compiled function = mock(FunctionElement.Compiled.class);
    private Filter underTest = new Filter(position, expression)
                                .withContent(new Sequence());

    @Before
    public void setUp() throws Exception {
        when(compileContext.clone()).thenReturn(compileContext);
        when(function.calculate(renderContext)).thenReturn(null);
        when(expression.compile(compileContext)).thenReturn(new Filter.DelegateCalculable(function));
        when(renderContext.newRenderContext(any(OutputStream.class))).thenReturn(renderContext);
        when(function.cloneAndAddArgument(any(Expression.class))).thenReturn(function);
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
