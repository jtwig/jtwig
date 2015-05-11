/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.expressions.model;

import org.jtwig.compile.CompileContext;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.OperationNotFoundException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.expressions.api.UnaryOperation;
import org.jtwig.expressions.operations.UnaryOperator;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;

public class OperationUnary extends AbstractCompilableExpression {
    public static class Builder implements CompilableExpression {
        private JtwigPosition position;
        private Operator operator;
        private CompilableExpression operand;

        public Builder withPosition(JtwigPosition position) {
            this.position = position;
            return this;
        }

        public Builder withOperator(Operator operator) {
            this.operator = operator;
            return this;
        }

        public Builder withOperand(CompilableExpression operand) {
            this.operand = operand;
            return this;
        }

        public OperationUnary build () {
            return new OperationUnary(position, operator, operand);
        }

        @Override
        public Expression compile(CompileContext context) throws CompileException {
            return null;
        }
    }

    private final Operator operator;
    private CompilableExpression operand;

    public OperationUnary(JtwigPosition position, Operator operator, CompilableExpression operand) {
        super(position);
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public Expression compile(CompileContext context) throws CompileException {
        try {
            return new Operation(operand.compile(context), UnaryOperator.fromOperator(operator).operation());
        } catch (OperationNotFoundException e) {
            throw new CompileException(e);
        }
    }

    private static class Operation implements Expression {
        private final Expression operand;
        private final UnaryOperation operation;

        private Operation(Expression operand, UnaryOperation operation) {
            this.operand = operand;
            this.operation = operation;
        }

        @Override
        public Object calculate(RenderContext context) throws CalculateException {
            return operation.apply(context, operand);
        }
    }
}
