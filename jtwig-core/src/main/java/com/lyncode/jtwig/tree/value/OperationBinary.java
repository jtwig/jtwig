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
import com.lyncode.jtwig.tree.helper.StrictBinaryOperation;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class OperationBinary implements Calculable {
    private ValueList operands = new ValueList();
    private List<Operator> operators = new ArrayList<Operator>();


    public OperationBinary(Object operand) {
        operands.add(operand);
    }

    public ValueList getOperands() {
        return operands;
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public boolean addOperator (Operator operator) {
        operators.add(operator);
        return true;
    }

    public boolean add (Object operand) {
        operands.add(operand);
        return true;
    }
    public String toString () {
        List<String> results = new ArrayList<String>();
        if (!operands.getList().isEmpty()) {
            results.add(getDescription(0));
            for (int i=1;i<operands.getList().size();i++) {
                results.add(operators.get(i-1).toString());
                results.add(getDescription(i));
            }
        } else return "Binary operation without operands";

        return StringUtils.join(results, " ");
    }

    private String getDescription(int index) {
        Object element = operands.getList().get(index);
        if (element instanceof OperationBinary)
            return "(" + element.toString() + ")";
        else
            return element.toString();
    }

    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        return StrictBinaryOperation.create(this).calculate(context);
    }
}
