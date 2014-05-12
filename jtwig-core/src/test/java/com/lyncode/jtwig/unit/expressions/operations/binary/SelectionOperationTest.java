package com.lyncode.jtwig.unit.expressions.operations.binary;

import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.expressions.model.FunctionElement;
import com.lyncode.jtwig.expressions.model.Variable;
import com.lyncode.jtwig.expressions.operations.binary.SelectionOperation;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.render.config.RenderConfiguration;
import org.junit.Test;

import java.util.Arrays;

import static com.lyncode.jtwig.types.Undefined.UNDEFINED;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SelectionOperationTest {
    private SelectionOperation operation = new SelectionOperation();

    @Test(expected = CalculateException.class)
    public void accessPropertyOfNullWithStrictMode() throws Exception {
        RenderContext renderContext = mock(RenderContext.class);
        Expression left = mock(Expression.class);

        JtwigPosition position = new JtwigPosition(null, 1, 1);
        Expression right = new Variable.Compiled(position, "variable");

        when(renderContext.configuration()).thenReturn(new RenderConfiguration().strictMode(true));
        when(left.calculate(renderContext)).thenReturn(null);

        operation.apply(renderContext, position, left, right);
    }

    @Test
    public void accessPropertyOfNullWithoutStrictMode() throws Exception {
        RenderContext renderContext = mock(RenderContext.class);
        Expression left = mock(Expression.class);

        JtwigPosition position = new JtwigPosition(null, 1, 1);
        Expression right = new Variable.Compiled(position, "variable");

        when(renderContext.configuration()).thenReturn(new RenderConfiguration().strictMode(false));
        when(left.calculate(renderContext)).thenReturn(null);

        Object result = operation.apply(renderContext, position, left, right);
        assertEquals(UNDEFINED, result);
    }

    @Test(expected = CalculateException.class)
    public void accessMethodOfNullWithStrictMode() throws Exception {
        RenderContext renderContext = mock(RenderContext.class);
        Expression left = mock(Expression.class);

        JtwigPosition position = new JtwigPosition(null, 1, 1);
        Expression right = new FunctionElement.Compiled(position, "variable", Arrays.<Expression>asList());

        when(renderContext.configuration()).thenReturn(new RenderConfiguration().strictMode(true));
        when(left.calculate(renderContext)).thenReturn(null);

        operation.apply(renderContext, position, left, right);
    }

    @Test
    public void accessMethodOfNullWithoutStrictMode() throws Exception {
        RenderContext renderContext = mock(RenderContext.class);
        Expression left = mock(Expression.class);

        JtwigPosition position = new JtwigPosition(null, 1, 1);
        Expression right = new FunctionElement.Compiled(position, "variable", Arrays.<Expression>asList());

        when(renderContext.configuration()).thenReturn(new RenderConfiguration().strictMode(false));
        when(left.calculate(renderContext)).thenReturn(null);

        Object result = operation.apply(renderContext, position, left, right);
        assertEquals(UNDEFINED, result);
    }

    @Test(expected = CalculateException.class)
    public void operationShouldFailIfRightHandSideIsNotAFunctionNeitherAVariable() throws Exception {
        RenderContext renderContext = mock(RenderContext.class);
        Expression left = mock(Expression.class);
        JtwigPosition position = new JtwigPosition(null, 1, 1);

        when(renderContext.configuration()).thenReturn(new RenderConfiguration().strictMode(false));
        when(left.calculate(renderContext)).thenReturn("hello");

        operation.apply(renderContext, position, left, null);
    }
}