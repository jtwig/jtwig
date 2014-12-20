package org.jtwig.unit.expressions.operations.binary;

import org.jtwig.exception.CalculateException;
import org.jtwig.expressions.api.Expression;
import org.jtwig.expressions.model.FunctionElement;
import org.jtwig.expressions.model.Variable;
import org.jtwig.expressions.operations.binary.SelectionOperation;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.jtwig.render.config.RenderConfiguration;
import org.jtwig.util.ObjectExtractor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import org.jtwig.cache.impl.ExecutionCache;

import static org.jtwig.types.Undefined.UNDEFINED;
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
        when(renderContext.configuration()).thenReturn(config().strictMode(true));
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

        when(renderContext.configuration()).thenReturn(config().strictMode(false));

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
        when(renderContext.configuration()).thenReturn(config().strictMode(false));

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
        when(renderContext.configuration()).thenReturn(config().strictMode(false));
        when(left.calculate(renderContext)).thenReturn(UNDEFINED);

        Object result = operation.apply(renderContext, position, left, right);
        assertEquals(UNDEFINED, result);
    }

    @Test(expected = CalculateException.class)
    public void accessMethodOfUndefinedWithStrictMode() throws Exception {
        Expression right = new FunctionElement.Compiled(position, "variable", Arrays.<Expression>asList());
        when(left.calculate(renderContext)).thenReturn(UNDEFINED);
        when(renderContext.configuration()).thenReturn(config().strictMode(true));

        operation.apply(renderContext, position, left, right);
    }

    @Test
    public void accessMethodOfUndefinedWithoutStrictMode() throws Exception {
        Expression right = new FunctionElement.Compiled(position, "variable", Arrays.<Expression>asList());
        when(renderContext.configuration()).thenReturn(config().strictMode(false));
        when(left.calculate(renderContext)).thenReturn(UNDEFINED);

        Object result = operation.apply(renderContext, position, left, right);
        assertEquals(UNDEFINED, result);
    }

    @Test(expected = CalculateException.class)
    public void operationShouldFailIfRightHandSideIsNotAFunctionNeitherAVariable() throws Exception {
        when(renderContext.configuration()).thenReturn(config().strictMode(false));
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
    private RenderConfiguration config() {
        return new RenderConfiguration(new ExecutionCache());
    }
}
