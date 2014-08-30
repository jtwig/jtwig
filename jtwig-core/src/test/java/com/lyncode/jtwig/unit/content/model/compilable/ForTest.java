package com.lyncode.jtwig.unit.content.model.compilable;

import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.model.compilable.For;
import com.lyncode.jtwig.content.model.compilable.Sequence;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.render.RenderContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ForTest {
    private final CompilableExpression compilableExpression = mock(CompilableExpression.class);
    private final CompileContext compileContext = mock(CompileContext.class);
    private final RenderContext renderContext = mock(RenderContext.class);
    private For underTest = new For("test", compilableExpression)
            .withContent(new Sequence());

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        when(compileContext.clone()).thenReturn(compileContext);
    }

    @Test(expected = RenderException.class)
    public void renderWhenCalculateExceptions() throws Exception {
        when(compilableExpression.compile(any(CompileContext.class)))
                .thenReturn(expressionToReturn(expressionToReturn(expressionThrowsException())));

        underTest.compile(compileContext)
                .render(renderContext);
    }

    @Test
    public void resolvedValueIsArrayDoNotThrowsException() throws Exception {
        Expression expression = mock(Expression.class);
        when(compilableExpression.compile(any(CompileContext.class)))
                .thenReturn(expression);
        when(expression.calculate(any(RenderContext.class)))
                .thenReturn(new int[]{1, 2, 3});

        underTest.compile(compileContext)
                .render(renderContext);
    }

    @Test
    public void resolvedValueNonListNeitherMapThrowsException() throws Exception {
        Expression expression = mock(Expression.class);
        when(compilableExpression.compile(any(CompileContext.class)))
                .thenReturn(expression);
        when(expression.calculate(any(RenderContext.class)))
                .thenReturn(1);

        expectedException.expect(RenderException.class);
        expectedException.expectMessage(startsWith("Expecting a map as parameter for the loop but"));

        underTest.compile(compileContext)
                .render(renderContext);
    }



    private Expression expressionThrowsException() {
        return new Expression() {
            @Override
            public Object calculate(RenderContext context) throws CalculateException {
                throw new CalculateException();
            }
        };
    }

    private Expression expressionToReturn(final Expression expression) {
        return new Expression() {
            @Override
            public Object calculate(RenderContext context) throws CalculateException {
                return expression;
            }
        };
    }
}