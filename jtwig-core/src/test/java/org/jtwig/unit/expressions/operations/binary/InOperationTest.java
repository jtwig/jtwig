package org.jtwig.unit.expressions.operations.binary;

import org.jtwig.expressions.api.Expression;
import org.jtwig.expressions.operations.binary.InOperation;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.jtwig.types.Undefined;
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
