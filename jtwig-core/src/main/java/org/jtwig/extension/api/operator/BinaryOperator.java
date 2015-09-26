/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.extension.api.operator;

import org.jtwig.Environment;
import org.jtwig.compile.CompileContext;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.expressions.api.Expression;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.render.RenderContext;
import static org.jtwig.types.Undefined.UNDEFINED;
import org.parboiled.Rule;

public abstract class BinaryOperator extends AbstractOperator {
    public BinaryOperator(final String name, final int precedence) {
        super(name, precedence);
    }
    
    public Rule getRightSideRule(JtwigExpressionParser expr, Environment env) {
        return null;
    }

    @Override
    public Expression compile(final Environment env, final JtwigPosition pos,
            final CompileContext ctx, final Object... args)
            throws CompileException {
        assert args.length == 2;
        
        final Expression left = (Expression)args[0];
        final Expression right = (Expression)args[1];
        return new BinaryCallback(pos, left, right);
    }
    
    public abstract Object render(RenderContext ctx, JtwigPosition pos, Object left, Object right) throws CalculateException;
    
    public class BinaryCallback implements Expression {
        protected final JtwigPosition pos;
        protected final Expression left;
        protected final Expression right;
        
        public BinaryCallback(final JtwigPosition pos, final Expression left, final Expression right) {
            this.pos = pos;
            this.left = left;
            this.right = right;
        }

        @Override
        public Object calculate(final RenderContext context) throws CalculateException {
            Object calculatedLeft = left.calculate(context);
            Object calculatedRight = right.calculate(context);

            if (calculatedLeft == UNDEFINED)
                calculatedLeft = null;
            if (calculatedRight == UNDEFINED)
                calculatedRight = null;
            return BinaryOperator.this.render(context, pos, calculatedLeft, calculatedRight);
        }
        
    }
    
}