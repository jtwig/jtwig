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

import org.jtwig.exception.OperationNotFoundException;
import org.jtwig.expressions.api.UnaryOperation;
import org.jtwig.expressions.model.Operator;
import org.jtwig.expressions.operations.unary.NegativeOperation;
import org.jtwig.expressions.operations.unary.NotOperation;

public enum UnaryOperator {
    NOT(Operator.NOT, new NotOperation()),
    NEGATIVE(Operator.SUB, new NegativeOperation())
    ;

    public static UnaryOperator fromOperator (Operator operator) throws OperationNotFoundException {
        for (UnaryOperator unary : values()) {
            if (unary.operator == operator)
                return unary;
        }

        throw new OperationNotFoundException("Unable to find operator "+operator);
    }

    private final Operator operator;
    private final UnaryOperation operation;

    UnaryOperator(Operator operator, UnaryOperation operation) {
        this.operator = operator;
        this.operation = operation;
    }

    public UnaryOperation operation() {
        return operation;
    }
}
