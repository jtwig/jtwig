package com.lyncode.jtwig.expressions.api;

import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.parser.model.JtwigPosition;

public interface BinaryExpressionFactory {
    Expression expression(JtwigPosition position, Expression left, Expression right) throws CompileException;
}
