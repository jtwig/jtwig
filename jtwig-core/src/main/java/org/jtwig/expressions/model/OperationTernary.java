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

import org.jtwig.compile.CompileContext;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import static org.jtwig.util.TypeUtil.toBoolean;

public class OperationTernary extends AbstractCompilableExpression {
    private final CompilableExpression condition;
    private CompilableExpression ifTrueExpression;
    private CompilableExpression ifFalseExpression;

    public OperationTernary(JtwigPosition position, CompilableExpression booleanExpression) {
        super(position);
        this.condition = booleanExpression;
    }

    public OperationTernary withTrueExpression(CompilableExpression ifTrueExpression) {
        this.ifTrueExpression = ifTrueExpression;
        return this;
    }

    public OperationTernary withFalseExpression(CompilableExpression ifFalseExpression) {
        this.ifFalseExpression = ifFalseExpression;
        return this;
    }

    @Override
    public Expression compile(CompileContext context) throws CompileException {
        return new Compiled(condition.compile(context),
                ifTrueExpression.compile(context),
                ifFalseExpression.compile(context));
    }

    private static class Compiled implements Expression {
        private final Expression condition;
        private final Expression trueExpression;
        private final Expression falseExpression;

        private Compiled(Expression condition, Expression trueExpression, Expression falseExpression) {
            this.condition = condition;
            this.trueExpression = trueExpression;
            this.falseExpression = falseExpression;
        }

        @Override
        public Object calculate(RenderContext context) throws CalculateException {
            if (toBoolean(condition.calculate(context))) {
                return trueExpression.calculate(context);
            } else return falseExpression.calculate(context);
        }
    }
}
