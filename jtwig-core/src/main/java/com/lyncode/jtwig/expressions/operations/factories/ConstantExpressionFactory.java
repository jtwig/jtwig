package com.lyncode.jtwig.expressions.operations.factories;

import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.expressions.api.BinaryExpressionFactory;
import com.lyncode.jtwig.expressions.api.BinaryOperation;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;

public class ConstantExpressionFactory implements BinaryExpressionFactory {
    public static ConstantExpressionFactory operation (BinaryOperation operation) {
        return new ConstantExpressionFactory(operation);
    }

    private final BinaryOperation operation;

    public ConstantExpressionFactory(BinaryOperation operation) {
        this.operation = operation;
    }

    @Override
    public Expression expression(final JtwigPosition position, final Expression left, final Expression right) throws CompileException {
        return new Expression() {
            @Override
            public Object calculate(RenderContext context) throws CalculateException {
                return operation.apply(context, position, left, right);
            }
        };
    }
}
