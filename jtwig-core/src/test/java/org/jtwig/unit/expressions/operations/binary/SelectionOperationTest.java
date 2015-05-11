package org.jtwig.unit.expressions.operations.binary;

import java.util.Arrays;

import org.jtwig.configuration.JtwigConfigurationBuilder;
import org.jtwig.unit.AbstractJtwigTest;
import org.jtwig.exception.CalculateException;
import org.jtwig.expressions.api.Expression;
import org.jtwig.expressions.model.Constant;
import org.jtwig.expressions.model.FunctionElement;
import org.jtwig.expressions.model.Variable;
import org.jtwig.expressions.operations.binary.SelectionOperation;
import org.jtwig.parser.model.JtwigPosition;

import static org.jtwig.configuration.JtwigConfigurationBuilder.newConfiguration;
import static org.jtwig.types.Undefined.UNDEFINED;
import org.jtwig.util.ObjectExtractor;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
import org.hamcrest.core.IsInstanceOf;

public class SelectionOperationTest extends AbstractJtwigTest {
    private JtwigPosition position = new JtwigPosition(null, 1, 1);
    private Expression left = mock(Expression.class);
    private SelectionOperation operation = new SelectionOperation();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        super.before();
        when(theEnvironment().getConfiguration()).thenReturn(newConfiguration()
                                                                 .withStrictMode(true)
                                                                 .build());
        
        when(left.calculate(renderContext)).thenReturn(null);
    }

    @Test(expected = CalculateException.class)
    public void accessPropertyOfNullWithStrictMode() throws Exception {
        Expression right = new Variable.Compiled(position, "variable");

        operation.apply(renderContext, position, left, right);
    }

    @Test
    public void accessPropertyOfNullWithoutStrictMode() throws Exception {
        when(theEnvironment().getConfiguration()).thenReturn(newConfiguration()
                                                                 .withStrictMode(false)
                                                                 .build());
        Expression right = new Variable.Compiled(position, "variable");

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
        when(theEnvironment().getConfiguration()).thenReturn(newConfiguration()
                                                                 .withStrictMode(false)
                                                                 .build());
        Expression right = new FunctionElement.Compiled(position, "variable", Arrays.<Expression>asList());

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
        when(theEnvironment().getConfiguration()).thenReturn(newConfiguration()
                                                                 .withStrictMode(false)
                                                                 .build());
        Expression right = new Variable.Compiled(position, "variable");
        when(left.calculate(renderContext)).thenReturn(UNDEFINED);

        Object result = operation.apply(renderContext, position, left, right);
        assertEquals(UNDEFINED, result);
    }

    @Test(expected = CalculateException.class)
    public void accessMethodOfUndefinedWithStrictMode() throws Exception {
        Expression right = new FunctionElement.Compiled(position, "variable", Arrays.<Expression>asList());
        when(left.calculate(renderContext)).thenReturn(UNDEFINED);

        operation.apply(renderContext, position, left, right);
    }

    @Test
    public void accessMethodOfUndefinedWithoutStrictMode() throws Exception {
        when(theEnvironment().getConfiguration()).thenReturn(newConfiguration()
                                                                 .withStrictMode(false)
                                                                 .build());
        Expression right = new FunctionElement.Compiled(position, "variable", Arrays.<Expression>asList());
        when(left.calculate(renderContext)).thenReturn(UNDEFINED);

        Object result = operation.apply(renderContext, position, left, right);
        assertEquals(UNDEFINED, result);
    }

    @Test(expected = CalculateException.class)
    public void operationShouldFailIfRightHandSideIsNotAFunctionNeitherAVariable() throws Exception {
        when(theEnvironment().getConfiguration()).thenReturn(newConfiguration()
                                                                 .withStrictMode(false)
                                                                 .build());
        when(left.calculate(renderContext)).thenReturn("hello");

        operation.apply(renderContext, position, left, null);
    }

    @Test
    public void extractExceptionThrown() throws Exception {
        expectedException.expectCause(is(IsInstanceOf.<Throwable>instanceOf(ObjectExtractor.ExtractException.class)));

        operation.apply(renderContext, position, new Constant(new Object()), variable());
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
