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

import com.google.common.base.Function;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.OperationNotFoundException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.expressions.operations.BinaryOperator;
import com.lyncode.jtwig.parser.model.JtwigPosition;

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

        if (operands.size() == 1) return operands.get(0).compile(context);
        else {
            Expression left = operands.get(0).compile(context);
            for (int i = 1; i < operands.size(); i++) {
                Expression right = operands.get(i).compile(context);
                Operator operator = operators.get(i - 1);

                try {
                    left = BinaryOperator
                            .fromOperator(operator)
                            .expression(position(), left, right);
                } catch (OperationNotFoundException e) {
                    throw new CompileException(position()+": "+ e.getMessage());
                }
            }
            return left;
        }
    }

    public void transformFirst(Function<CompilableExpression, CompilableExpression> transformation) {
        operands.set(0, transformation.apply(operands.get(0)));
    }
}
