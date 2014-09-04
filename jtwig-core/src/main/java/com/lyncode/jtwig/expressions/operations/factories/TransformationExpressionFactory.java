package com.lyncode.jtwig.expressions.operations.factories;

import com.google.common.base.Function;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.expressions.api.BinaryExpressionFactory;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;

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
