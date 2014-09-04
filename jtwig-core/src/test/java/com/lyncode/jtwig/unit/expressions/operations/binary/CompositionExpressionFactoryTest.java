package com.lyncode.jtwig.unit.expressions.operations.binary;

import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.expressions.operations.factories.CompositionExpressionFactory;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.mockito.Mockito.mock;

public class CompositionExpressionFactoryTest {
    private final JtwigPosition jtwigPosition = mock(JtwigPosition.class);
    private final Expression leftExpression = mock(Expression.class);
    private final Expression rightExpression = mock(Expression.class);
    private CompositionExpressionFactory underTest = new CompositionExpressionFactory();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void compositionWithoutFunctionOrVariableOnTheRightSide() throws Exception {
        expectedException.expect(CompileException.class);
        expectedException.expectMessage(endsWith("Composition always requires a function to execute as the right argument"));

        underTest.expression(jtwigPosition, leftExpression, rightExpression);
    }
}