package org.jtwig.expressions.operations.factories;

import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.expressions.api.BinaryExpressionFactory;
import org.jtwig.expressions.api.BinaryOperation;
import org.jtwig.expressions.api.Expression;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;

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
