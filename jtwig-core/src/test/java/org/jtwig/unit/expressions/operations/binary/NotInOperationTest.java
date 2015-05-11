package org.jtwig.unit.expressions.operations.binary;

import org.hamcrest.core.Is;
import org.jtwig.expressions.api.Expression;
import org.jtwig.expressions.operations.binary.NotInOperation;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NotInOperationTest {
    private NotInOperation underTest = new NotInOperation();
    private JtwigPosition jtwigPosition;

    @Test
    public void applyTest() throws Exception {
        jtwigPosition = mock(JtwigPosition.class);
        RenderContext renderContext = mock(RenderContext.class);
        Expression left = mock(Expression.class);
        Expression right = mock(Expression.class);
        when(left.calculate(renderContext)).thenReturn(3);
        when(right.calculate(renderContext)).thenReturn(asList(4, 5));

        Object result = underTest.apply(renderContext, jtwigPosition, left, right);

        assertThat(result, Is.<Object>is(true));
    }
}
