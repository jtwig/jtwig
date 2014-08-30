package com.lyncode.jtwig.unit.expressions.operations.binary;

import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.expressions.operations.binary.InOperation;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.types.Undefined;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InOperationTest {
    private final RenderContext renderContext = mock(RenderContext.class);
    private final JtwigPosition jtwigPosition = mock(JtwigPosition.class);
    private final Expression leftExpression = mock(Expression.class);
    private final Expression rightExpression = mock(Expression.class);
    private InOperation underTest = new InOperation();

    @Test
    public void rightUndefinedSameAsNull() throws Exception {
        when(rightExpression.calculate(renderContext)).thenReturn(Undefined.UNDEFINED);
        assertEquals(false, underTest.apply(renderContext, jtwigPosition, leftExpression, rightExpression));
    }

    @Test
    public void rightNonArrayNeitherString() throws Exception {
        when(rightExpression.calculate(renderContext)).thenReturn(1);

        assertEquals(false, underTest.apply(renderContext, jtwigPosition, leftExpression, rightExpression));
    }
}