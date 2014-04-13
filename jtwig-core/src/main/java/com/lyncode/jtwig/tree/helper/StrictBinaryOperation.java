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

package com.lyncode.jtwig.tree.helper;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.functions.util.ObjectIterator;
import com.lyncode.jtwig.parser.positioning.Position;
import com.lyncode.jtwig.tree.api.Expression;
import com.lyncode.jtwig.tree.expressions.Composition;
import com.lyncode.jtwig.tree.expressions.OperationBinary;
import com.lyncode.jtwig.tree.expressions.Operator;
import com.lyncode.jtwig.tree.expressions.Selection;
import com.lyncode.jtwig.unit.util.MathOperations;
import com.lyncode.jtwig.unit.util.RelationalOperations;

import static com.lyncode.jtwig.unit.util.BooleanOperations.*;
import static com.lyncode.jtwig.unit.util.RelationalOperations.*;

public class StrictBinaryOperation implements Expression {
    private Operator operator;
    private Expression left;
    private Expression right;
    private Position position;


    public static StrictBinaryOperation create(OperationBinary binary) {
        int index = 0;
        StrictBinaryOperation last = null;
        for (Operator operator : binary.getOperators()) {
            StrictBinaryOperation operation = new StrictBinaryOperation();
            operation.operator = operator;
            if (last != null)
                operation.left = last;
            else
                operation.left = binary.getOperands().get(index);

            operation.right = binary.getOperands().get(index+1);
            last = operation;
            index++;
        }
        last.position = binary.getPosition();
        return last;
    }
    private Object numericExecute(JtwigContext resolver) throws CalculateException {
        switch (operator) {
            case ADD:
                return MathOperations.sum(left.calculate(resolver), right.calculate(resolver));
            case SUB:
                return MathOperations.sub(left.calculate(resolver), right.calculate(resolver));
            case DIV:
                return MathOperations.div(((Number)left.calculate(resolver)).doubleValue(), ((Number)right.calculate(resolver)).doubleValue());
            case INT_DIV:
                return MathOperations.div(left.calculate(resolver), right.calculate(resolver));
            case TIMES:
                return MathOperations.mul(left.calculate(resolver), right.calculate(resolver));
            case MOD:
                return MathOperations.mod(left.calculate(resolver), right.calculate(resolver));
        }
        throw new CalculateException(position+": Expression with unknown operator " + operator.toString());
    }

    private Object relationalExecute(JtwigContext resolver) throws CalculateException {
        Object leftResolved = left.calculate(resolver);
        Object rightResolved = right.calculate(resolver);
        switch (operator) {
            case GT:
                return gt(leftResolved, rightResolved);
            case GTE:
                return gte(leftResolved, rightResolved);
            case LT:
                return lt(leftResolved, rightResolved);
            case LTE:
                return lte(leftResolved, rightResolved);
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
            case IN:
                if (rightResolved == null) return false;
                if ((rightResolved instanceof Iterable) || rightResolved.getClass().isArray())
                    return new ObjectIterator(rightResolved).contains(leftResolved);
                else if (rightResolved instanceof String)
                    return ((String) rightResolved).contains(leftResolved.toString());
                else
                    return false;
        }
        throw new CalculateException(position+": Expression with unknown operator " + operator.toString());
    }

    private Object booleanExecute(JtwigContext resolver) throws CalculateException {
        switch (operator) {
            case AND:
                return and(left.calculate(resolver), right.calculate(resolver));
            case OR:
                return or(left.calculate(resolver), right.calculate(resolver));
        }
        throw new CalculateException(position+": Expression with unknown operator " + operator.toString());
    }


    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        switch (operator) {
            case COMPOSITION:
                Composition composition = new Composition(position, left);
                composition.add(right);
                return composition.calculate(context);
            case SELECTION:
                Selection selection = new Selection(position);
                selection.add(left);
                selection.add(right);
                return selection.calculate(context);
            case IS:
                composition = new Composition(position, left);
                composition.add(right);
                return isTrue(composition.calculate(context));
            case IS_NOT:
                composition = new Composition(position, left);
                composition.add(right);
                return !isTrue(composition.calculate(context));
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
