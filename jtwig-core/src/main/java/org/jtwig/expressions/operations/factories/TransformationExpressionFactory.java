package org.jtwig.expressions.operations.factories;

import com.google.common.base.Function;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.expressions.api.BinaryExpressionFactory;
import org.jtwig.expressions.api.Expression;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;

public class TransformationExpressionFactory implements BinaryExpressionFactory {
    private final BinaryExpressionFactory delegate;
    private final Function<Object, Object> function;

    public TransformationExpressionFactory(BinaryExpressionFactory delegate, Function<Object, Object> function) {
        this.delegate = delegate;
        this.function = function;
    }

    @Override
    public Expression expression(JtwigPosition position, Expression left, Expression right) throws CompileException {
        return new Compiled(delegate.expression(position, left, right), function);
    }

    private class Compiled implements Expression {
        private final Expression expression;
        private final Function<Object, Object> function;

        public Compiled(Expression expression, Function<Object, Object> function) {
            this.expression = expression;
            this.function = function;
        }

        @Override
        public Object calculate(RenderContext context) throws CalculateException {
            return function.apply(expression.calculate(context));
        }
    }
}
