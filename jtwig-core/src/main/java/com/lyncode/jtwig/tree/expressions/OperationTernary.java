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

package com.lyncode.jtwig.tree.expressions;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.tree.api.Expression;

import static com.lyncode.jtwig.util.BooleanOperations.isTrue;

public class OperationTernary implements Expression {
    private Expression booleanExpression;
    private Expression ifTrueExpression;
    private Expression ifFalseExpression;

    public OperationTernary(Expression booleanExpression) {
        this.booleanExpression = booleanExpression;
    }

    public boolean setIfTrueExpression(Expression ifTrueExpression) {
        this.ifTrueExpression = ifTrueExpression;
        return true;
    }

    public boolean setIfFalseExpression(Expression ifFalseExpression) {
        this.ifFalseExpression = ifFalseExpression;
        return true;
    }

    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        if (isTrue(booleanExpression.calculate(context)))
            return ifTrueExpression.calculate(context);
        else
            return ifFalseExpression.calculate(context);
    }
}
