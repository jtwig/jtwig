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

package com.lyncode.jtwig.tree.helper;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.tree.api.Calculable;
import com.lyncode.jtwig.tree.value.OperationBinary;
import com.lyncode.jtwig.tree.value.Operator;
import com.lyncode.jtwig.util.BooleanOperations;
import com.lyncode.jtwig.util.MathOperations;
import com.lyncode.jtwig.util.RelationalOperations;

public class StrictBinaryOperation implements Calculable {
    private Operator operator;
    private Object left;
    private Object right;


    public static StrictBinaryOperation create(OperationBinary binary) {
        int index = 0;
        StrictBinaryOperation last = null;
        for (Operator operator : binary.getOperators()) {
            StrictBinaryOperation operation = new StrictBinaryOperation();
            operation.operator = operator;
            if (last != null)
                operation.left = last;
            else
                operation.left = binary.getOperands().getList().get(index);

            operation.right = binary.getOperands().getList().get(index+1);
            last = operation;
            index++;
        }
        return last;
    }
    private Object numericExecute(JtwigContext resolver) throws CalculateException {
        switch (operator) {
            case ADD:
                return MathOperations.sum(resolver.resolve(left), resolver.resolve(right));
            case SUB:
                return MathOperations.sub(resolver.resolve(left), resolver.resolve(right));
            case DIV:
                return MathOperations.div(resolver.resolve(left), resolver.resolve(right));
            case TIMES:
                return MathOperations.mul(resolver.resolve(left), resolver.resolve(right));
            case MOD:
                return MathOperations.mod(resolver.resolve(left), resolver.resolve(right));
        }
        throw new CalculateException("Unknown operator " + operator.toString());
    }

    private Object relationalExecute(JtwigContext resolver) throws CalculateException {
        Object leftResolved = resolver.resolve(left);
        Object rightResolved = resolver.resolve(right);
        switch (operator) {
            case GT:
                return RelationalOperations.gt(leftResolved, rightResolved);
            case GTE:
                return RelationalOperations.gte(leftResolved, rightResolved);
            case LT:
                return RelationalOperations.lt(leftResolved, rightResolved);
            case LTE:
                return RelationalOperations.lte(leftResolved, rightResolved);
            case EQUAL:
                return RelationalOperations.eq(leftResolved, rightResolved);
            case DIFF:
                return RelationalOperations.neq(leftResolved, rightResolved);
            case STARTS_WITH:
                if (leftResolved == null) return false;
                return leftResolved.toString().startsWith(rightResolved.toString());
            case ENDS_WITH:
                if (leftResolved == null) return false;
                return leftResolved.toString().endsWith(rightResolved.toString());
            case MATCHES:
                if (leftResolved == null) return false;
                return leftResolved.toString().matches(rightResolved.toString());
        }
        throw new CalculateException("Unknown operator " + operator.toString());
    }

    private Object booleanExecute(JtwigContext resolver) throws CalculateException {
        switch (operator) {
            case AND:
                return BooleanOperations.and(resolver.resolve(left), resolver.resolve(right));
            case OR:
                return BooleanOperations.or(resolver.resolve(left), resolver.resolve(right));
        }
        throw new CalculateException("Unknown operator " + operator.toString());
    }


    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        switch (operator) {
            case ADD:
            case SUB:
            case TIMES:
            case DIV:
            case MOD:
            case INT_DIV:
            case INT_TIMES:
                return numericExecute(context);
            case AND:
            case OR:
                return booleanExecute(context);
            default:
                return relationalExecute(context);
        }
    }
}
