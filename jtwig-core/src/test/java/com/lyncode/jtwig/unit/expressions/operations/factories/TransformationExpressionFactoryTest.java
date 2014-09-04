package com.lyncode.jtwig.unit.expressions.operations.factories;

import com.google.common.base.Function;
import com.lyncode.jtwig.expressions.api.BinaryExpressionFactory;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.expressions.operations.factories.TransformationExpressionFactory;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TransformationExpressionFactoryTest {
    private final BinaryExpressionFactory expressionFactory = mock(BinaryExpressionFactory.class);
    private final Function transformation = mock(Function.class);
    private final JtwigPosition position = mock(JtwigPosition.class);
    private final RenderContext renderContext = mock(RenderContext.class);
    private final Expression left = mock(Expression.class);
    private final Expression right = mock(Expression.class);
    private TransformationExpressionFactory underTest = new TransformationExpressionFactory(expressionFactory, transformation);

    @Test
    public void delegatesExpressionToDelegate() throws Exception {
        underTest.expression(position, left, right);
        verify(expressionFactory).expression(position, left, right);
    }

    @Test
    public void appliesFunctionToResultOfExpression() throws Exception {
        Object result = new Object();

        when(expressionFactory.expression(position, left, right)).thenReturn(left);
        when(left.calculate(renderContext)).thenReturn(result);

        underTest.expression(position, left, right)
                .calculate(renderContext);

        verify(transformation).apply(result);
    }
}