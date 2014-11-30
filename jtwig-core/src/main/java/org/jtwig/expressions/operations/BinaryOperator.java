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

package org.jtwig.expressions.operations;

import com.google.common.base.Function;
import org.jtwig.exception.OperationNotFoundException;
import org.jtwig.expressions.api.BinaryExpressionFactory;
import org.jtwig.expressions.model.Operator;
import org.jtwig.expressions.operations.binary.*;
import org.jtwig.expressions.operations.factories.CompositionExpressionFactory;
import org.jtwig.expressions.operations.factories.TransformationExpressionFactory;

import static org.jtwig.expressions.operations.factories.ConstantExpressionFactory.operation;
import static org.jtwig.util.BooleanOperations.isTrue;


public enum BinaryOperator {
    // Jtwig Operations
    COMPOSITION(Operator.COMPOSITION, new CompositionExpressionFactory()),
    SELECTION(Operator.SELECTION, operation(new SelectionOperation())),
    CONCATENATION(Operator.CONCATENATION, operation(new ConcatenationOperation())),

    // Math Operations
    SUM(Operator.ADD, operation(new SumOperation())),
    SUB(Operator.SUB, operation(new SubOperation())),
    MOD(Operator.MOD, operation(new ModOperation())),
    MUL(Operator.TIMES, operation(new MultOperation())),
    INT_MUL(Operator.INT_TIMES, operation(new IntMultOperation())),
    DIV(Operator.DIV, operation(new DivOperation())),
    INT_DIV(Operator.INT_DIV, operation(new IntDivOperation())),

    // Boolean Operations
    AND(Operator.AND, operation(new AndOperation())),
    OR(Operator.OR, operation(new OrOperation())),

    // Relational Operations
    GT(Operator.GT, operation(new GreaterThanOperation())),
    GTE(Operator.GTE, operation(new GreaterOrEqualThanOperation())),
    LT(Operator.LT, operation(new LessThanOperation())),
    LTE(Operator.LTE, operation(new LessOrEqualThanOperation())),
    EQUAL(Operator.EQUAL, operation(new EqualOperation())),
    DIFF(Operator.DIFF, operation(new DiffOperation())),

    // Other
    IN(Operator.IN, operation(new InOperation())),
    NOT_IN(Operator.NOT_IN, operation(new NotInOperation())),
    MATCHES(Operator.MATCHES, operation(new MatchesOperation())),
    ENDS_WITH(Operator.ENDS_WITH, operation(new EndsWithOperation())),
    STARTS_WITH(Operator.STARTS_WITH, operation(new StartsWithOperation())),
    IS(Operator.IS, new TransformationExpressionFactory(new CompositionExpressionFactory(), isTrueFunction())),
    IS_NOT(Operator.IS_NOT, new TransformationExpressionFactory(new CompositionExpressionFactory(), notIsTrueFunction()))
    ;

    public static BinaryExpressionFactory fromOperator(Operator operator) throws OperationNotFoundException {
        for (BinaryOperator binaryOperator : BinaryOperator.values()) {
            if (binaryOperator.operator == operator)
                return binaryOperator.expression;
        }

        throw new OperationNotFoundException(" Unable to find implementation for operator "+operator);
    }

    private Operator operator;
    private BinaryExpressionFactory expression;

    BinaryOperator(Operator operator, BinaryExpressionFactory expression) {
        this.operator = operator;
        this.expression = expression;
    }


    private static Function<Object, Object> notIsTrueFunction() {
        return new Function<Object, Object>() {
            @Override
            public Object apply(Object input) {
                return !isTrue(input);
            }
        };
    }

    private static Function<Object, Object> isTrueFunction() {
        return new Function<Object, Object>() {
            @Override
            public Object apply(Object input) {
                return isTrue(input);
            }
        };
    }
}
