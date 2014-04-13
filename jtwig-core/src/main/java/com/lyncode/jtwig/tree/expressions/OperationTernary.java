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
import com.lyncode.jtwig.parser.positioning.Position;
import com.lyncode.jtwig.tree.api.AbstractExpression;
import com.lyncode.jtwig.tree.api.Expression;

import static com.lyncode.jtwig.unit.util.BooleanOperations.isTrue;

public class OperationTernary extends AbstractExpression {
    private final Expression booleanExpression;
    private Expression ifTrueExpression;
    private Expression ifFalseExpression;

    public OperationTernary(Position position, Expression booleanExpression) {
        super(position);
        this.booleanExpression = booleanExpression;
    }

    public OperationTernary setIfTrueExpression(Expression ifTrueExpression) {
        this.ifTrueExpression = ifTrueExpression;
        return this;
    }

    public OperationTernary setIfFalseExpression(Expression ifFalseExpression) {
        this.ifFalseExpression = ifFalseExpression;
        return this;
    }

    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        if (isTrue(booleanExpression.calculate(context)))
            return ifTrueExpression.calculate(context);
        else
            return ifFalseExpression.calculate(context);
    }
}
