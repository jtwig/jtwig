package com.lyncode.jtwig.unit.expressions.operations.binary;

import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.expressions.operations.binary.CompositionOperation;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;

public class CompositionOperationTest {
    private final RenderContext renderContext = mock(RenderContext.class);
    private final JtwigPosition jtwigPosition = mock(JtwigPosition.class);
    private final Expression leftExpression = mock(Expression.class);
    private final Expression rightExpression = mock(Expression.class);
    private CompositionOperation underTest = new CompositionOperation();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void compositionWithoutFunctionOrVariableOnTheRightSide() throws Exception {
        expectedException.expect(CalculateException.class);
        expectedException.expectMessage(equalTo("Composition always requires a function to execute as the right argument"));

        underTest.apply(renderContext, jtwigPosition, leftExpression, rightExpression);
    }
}