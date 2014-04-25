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

package com.lyncode.jtwig.expressions.operations;

import com.lyncode.jtwig.exception.OperationNotFoundException;
import com.lyncode.jtwig.expressions.api.BinaryOperation;
import com.lyncode.jtwig.expressions.model.Operator;
import com.lyncode.jtwig.expressions.operations.binary.*;

public enum BinaryOperator {
    // Jtwig Operations
    COMPOSITION(Operator.COMPOSITION, new CompositionOperation()),
    SELECTION(Operator.SELECTION, new SelectionOperation()),

    // Math Operations
    SUM(Operator.ADD, new SumOperation()),
    SUB(Operator.SUB, new SubOperation()),
    MOD(Operator.MOD, new ModOperation()),
    MUL(Operator.TIMES, new MultOperation()),
    INT_MUL(Operator.INT_TIMES, new IntMultOperation()),
    DIV(Operator.DIV, new DivOperation()),
    INT_DIV(Operator.INT_DIV, new IntDivOperation()),

    // Boolean Operations
    AND(Operator.AND, new AndOperation()),
    OR(Operator.OR, new OrOperation()),

    // Relational Operations
    GT(Operator.GT, new GreaterThanOperation()),
    GTE(Operator.GTE, new GreaterOrEqualThanOperation()),
    LT(Operator.LT, new LessThanOperation()),
    LTE(Operator.LTE, new LessOrEqualThanOperation()),
    EQUAL(Operator.EQUAL, new EqualOperation()),

    // Other
    IN(Operator.IN, new InOperation()),
    MATCHES(Operator.MATCHES, new MatchesOperation()),
    ENDS_WITH(Operator.ENDS_WITH, new EndsWithOperation()),
    STARTS_WITH(Operator.STARTS_WITH, new StartsWithOperation()),
    IS(Operator.IS, new IsOperator()),
    IS_NOT(Operator.IS_NOT, new IsNotOperator())
    ;

    public static BinaryOperator fromOperator (Operator operator) throws OperationNotFoundException {
        for (BinaryOperator dictionary : BinaryOperator.values()) {
            if (dictionary.operator == operator)
                return dictionary;
        }

        throw new OperationNotFoundException(" Unable to find implementation for operator "+operator);
    }

    private Operator operator;
    private BinaryOperation operation;

    BinaryOperator(Operator operator, BinaryOperation operation) {
        this.operator = operator;
        this.operation = operation;
    }

    public BinaryOperation operation() {
        return operation;
    }
}
