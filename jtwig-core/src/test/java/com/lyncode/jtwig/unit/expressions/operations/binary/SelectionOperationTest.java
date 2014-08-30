package com.lyncode.jtwig.unit.expressions.operations.binary;

import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.expressions.model.FunctionElement;
import com.lyncode.jtwig.expressions.model.Variable;
import com.lyncode.jtwig.expressions.operations.binary.SelectionOperation;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.render.config.RenderConfiguration;
import com.lyncode.jtwig.util.ObjectExtractor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static com.lyncode.jtwig.types.Undefined.UNDEFINED;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SelectionOperationTest {
    private JtwigPosition position = new JtwigPosition(null, 1, 1);
    private Expression left = mock(Expression.class);
    private RenderContext renderContext = mock(RenderContext.class);
    private SelectionOperation operation = new SelectionOperation();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        when(renderContext.configuration()).thenReturn(new RenderConfiguration().strictMode(true));
        when(left.calculate(renderContext)).thenReturn(null);
    }

    @Test(expected = CalculateException.class)
    public void accessPropertyOfNullWithStrictMode() throws Exception {

        Expression right = new Variable.Compiled(position, "variable");

        operation.apply(renderContext, position, left, right);
    }

    @Test
    public void accessPropertyOfNullWithoutStrictMode() throws Exception {
        Expression right = new Variable.Compiled(position, "variable");

        when(renderContext.configuration()).thenReturn(new RenderConfiguration().strictMode(false));

        Object result = operation.apply(renderContext, position, left, right);
        assertEquals(UNDEFINED, result);
    }

    @Test(expected = CalculateException.class)
    public void accessMethodOfNullWithStrictMode() throws Exception {
        Expression right = new FunctionElement.Compiled(position, "variable", Arrays.<Expression>asList());
        operation.apply(renderContext, position, left, right);
    }

    @Test
    public void accessMethodOfNullWithoutStrictMode() throws Exception {
        Expression right = new FunctionElement.Compiled(position, "variable", Arrays.<Expression>asList());
        when(renderContext.configuration()).thenReturn(new RenderConfiguration().strictMode(false));

        Object result = operation.apply(renderContext, position, left, right);
        assertEquals(UNDEFINED, result);
    }

    @Test(expected = CalculateException.class)
    public void accessPropertyOfUndefinedWithStrictMode() throws Exception {
        Expression right = new Variable.Compiled(position, "variable");
        operation.apply(renderContext, position, left, right);
    }

    @Test
    public void accessPropertyOfUndefinedWithoutStrictMode() throws Exception {
        Expression right = new Variable.Compiled(position, "variable");
        when(renderContext.configuration()).thenReturn(new RenderConfiguration().strictMode(false));
        when(left.calculate(renderContext)).thenReturn(UNDEFINED);

        Object result = operation.apply(renderContext, position, left, right);
        assertEquals(UNDEFINED, result);
    }

    @Test(expected = CalculateException.class)
    public void accessMethodOfUndefinedWithStrictMode() throws Exception {
        Expression right = new FunctionElement.Compiled(position, "variable", Arrays.<Expression>asList());
        when(left.calculate(renderContext)).thenReturn(UNDEFINED);
        when(renderContext.configuration()).thenReturn(new RenderConfiguration().strictMode(true));

        operation.apply(renderContext, position, left, right);
    }

    @Test
    public void accessMethodOfUndefinedWithoutStrictMode() throws Exception {
        Expression right = new FunctionElement.Compiled(position, "variable", Arrays.<Expression>asList());
        when(renderContext.configuration()).thenReturn(new RenderConfiguration().strictMode(false));
        when(left.calculate(renderContext)).thenReturn(UNDEFINED);

        Object result = operation.apply(renderContext, position, left, right);
        assertEquals(UNDEFINED, result);
    }

    @Test(expected = CalculateException.class)
    public void operationShouldFailIfRightHandSideIsNotAFunctionNeitherAVariable() throws Exception {
        when(renderContext.configuration()).thenReturn(new RenderConfiguration().strictMode(false));
        when(left.calculate(renderContext)).thenReturn("hello");

        operation.apply(renderContext, position, left, null);
    }

    @Test
    public void extractExceptionThrown() throws Exception {
        expectedException.expect(CalculateException.class);

        operation.apply(renderContext, position, left, variable());
    }

    private Variable.Compiled variable() {
        return new Variable.Compiled(new JtwigPosition(null, 1,1), "test") {
            @Override
            public Object extract(ObjectExtractor extractor) throws ObjectExtractor.ExtractException {
                throw new ObjectExtractor.ExtractException("Test");
            }
        };
    }
}