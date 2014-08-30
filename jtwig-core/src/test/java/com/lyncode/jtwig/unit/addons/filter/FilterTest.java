package com.lyncode.jtwig.unit.addons.filter;

import com.lyncode.jtwig.addons.filter.Filter;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.model.compilable.Sequence;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.expressions.model.FunctionElement;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class FilterTest {
    private final CompilableExpression expression = mock(CompilableExpression.class);
    private final CompileContext compileContext = mock(CompileContext.class);
    private final RenderContext renderContext = mock(RenderContext.class);
    private Filter underTest = new Filter(expression)
                                .withContent(new Sequence());

    @Before
    public void setUp() throws Exception {
        when(compileContext.clone()).thenReturn(compileContext);
        when(expression.compile(compileContext)).thenReturn(new FunctionElement.Compiled(mock(JtwigPosition.class), "test", new ArrayList<Expression>()));
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