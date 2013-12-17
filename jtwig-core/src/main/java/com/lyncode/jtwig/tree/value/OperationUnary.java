/**
 * Copyright 2012 Lyncode
 *
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

package com.lyncode.jtwig.tree.value;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.tree.api.Calculable;
import com.lyncode.jtwig.util.BooleanOperations;

public class OperationUnary implements Calculable {
    private Operator operator;
    private Object operand;


    public Operator getOperator() {
        return operator;
    }

    public boolean setOperator(Operator operator) {
        this.operator = operator;
        return true;
    }

    public Object getOperand() {
        return operand;
    }

    public boolean setOperand(Object operand) {
        this.operand = operand;
        return true;
    }

    public String toString () {
        return operator.toString() + operand.toString();
    }

    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        switch (operator) {
            case NOT:
                return BooleanOperations.not(context.resolve(operand));
        }
        throw new CalculateException("Unknown operator "+operator);
    }
}
