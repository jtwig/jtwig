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

package com.lyncode.jtwig.expressions.model;

import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.OperationNotFoundException;
import com.lyncode.jtwig.expressions.api.BinaryOperation;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.expressions.operations.BinaryOperator;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;

import java.util.ArrayList;
import java.util.List;

public class OperationBinary extends AbstractCompilableExpression {
    private List<CompilableExpression> operands = new ArrayList<>();
    private List<Operator> operators = new ArrayList<Operator>();


    public OperationBinary(JtwigPosition position, CompilableExpression operand) {
        super(position);
        operands.add(operand);
    }

    public OperationBinary add (Operator operator) {
        operators.add(operator);
        return this;
    }

    public OperationBinary add (CompilableExpression operand) {
        operands.add(operand);
        return this;
    }

    @Override
    public Expression compile(CompileContext context) throws CompileException {
        assert operators.size() == operands.size() - 1;

        if (operands.isEmpty()) throw new CompileException(position()+" empty binary expression");
        if (operands.size() == 1) return operands.get(0).compile(context);
        else {
            Expression left = operands.get(0).compile(context);
            for (int i = 1; i < operands.size(); i++) {
                Expression right = operands.get(i).compile(context);
                Operator operator = operators.get(i - 1);

                try {
                    left = new Compiled(left, right, BinaryOperator.fromOperator(operator).operation());
                } catch (OperationNotFoundException e) {
                    throw new CompileException(position()+": "+ e.getMessage());
                }
            }
            return left;
        }
    }

    public static class Compiled implements Expression {
        private Expression leftOperand;
        private Expression rightOperand;
        private BinaryOperation operation;

        private Compiled(Expression leftOperand, Expression rightOperand, BinaryOperation operation) {
            this.leftOperand = leftOperand;
            this.rightOperand = rightOperand;
            this.operation = operation;
        }

        @Override
        public Object calculate(RenderContext context) throws CalculateException {
            return operation.apply(context, leftOperand, rightOperand);
        }

        public Expression left () { return leftOperand; }

        public Compiled left(Expression expression) {
            this.leftOperand = expression;
            return this;
        }
    }

}
