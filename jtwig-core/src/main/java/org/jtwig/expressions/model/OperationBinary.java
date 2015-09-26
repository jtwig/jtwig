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

import com.google.common.base.Function;
import org.jtwig.compile.CompileContext;
import org.jtwig.exception.CompileException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.parser.model.JtwigPosition;

import java.util.ArrayList;
import java.util.List;
import org.jtwig.extension.api.operator.Operator;

public class OperationBinary extends AbstractCompilableExpression {
    private List<CompilableExpression> operands = new ArrayList<>();
    private List<String> operators = new ArrayList<>();


    public OperationBinary(JtwigPosition position, CompilableExpression operand) {
        super(position);
        operands.add(operand);
    }

    public OperationBinary addOperator (String operator) {
        operators.add(operator);
        return this;
    }

    public OperationBinary addOperand (CompilableExpression operand) {
        operands.add(operand);
        return this;
    }

    @Override
    public Expression compile(CompileContext context) throws CompileException {
        assert operators.size() == operands.size() - 1;

        if (operands.size() == 1) {
            return operands.get(0).compile(context);
        }
        
        Expression left = operands.get(0).compile(context);
        for (int i = 1; i < operands.size(); i++) {
            Expression right = operands.get(i).compile(context);
            String operator = operators.get(i - 1);

            Operator op = context.environment()
                    .getConfiguration()
                    .getExtensions()
                    .getBinaryOperator(operator);
            if (op == null) {
                throw new CompileException(position()+": Could not find operator '"+operator+"'.");
            }
            left = op.compile(context.environment(), position(), context, left, right);
        }
        return left;
    }

    public void transformFirst(Function<CompilableExpression, CompilableExpression> transformation) {
        operands.set(0, transformation.apply(operands.get(0)));
    }
}
