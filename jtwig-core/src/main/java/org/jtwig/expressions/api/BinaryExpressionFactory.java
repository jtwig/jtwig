package org.jtwig.expressions.api;

import org.jtwig.exception.CompileException;
import org.jtwig.parser.model.JtwigPosition;

public interface BinaryExpressionFactory {
    Expression expression(JtwigPosition position, Expression left, Expression right) throws CompileException;
}
