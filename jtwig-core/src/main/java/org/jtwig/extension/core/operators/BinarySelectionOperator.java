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

package org.jtwig.extension.core.operators;

import org.jtwig.Environment;
import org.jtwig.compile.CompileContext;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.expressions.api.Expression;
import org.jtwig.expressions.api.Extractable;
import org.jtwig.expressions.model.Variable;
import org.jtwig.extension.api.operator.BinaryOperator;
import org.jtwig.extension.model.FunctionCall;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.render.RenderContext;
import org.jtwig.types.Undefined;
import static org.jtwig.types.Undefined.UNDEFINED;
import org.jtwig.util.ObjectExtractor;
import org.parboiled.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinarySelectionOperator extends BinaryOperator {
    private static final Logger LOGGER = LoggerFactory.getLogger(BinarySelectionOperator.class);
    
    public BinarySelectionOperator(final String name, final int precedence) {
        super(name, precedence);
    }
    
    @Override
    public Rule getRightSideRule(JtwigExpressionParser expr, Environment env) {
        return expr.FirstOf(
                expr.callableWithBrackets(FunctionCall.class),
                expr.mapEntry(),
                expr.variable()
        );
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
    
    @Override
    public Object render(RenderContext ctx, JtwigPosition pos, Object left, Object right) throws CalculateException {
        if (!(right instanceof Extractable)) {
            throw new CalculateException("Selection operator must be given a variable/function as right argument");
        }
        Extractable extractable = (Extractable)right;
        
        if (left == null || left instanceof Undefined) {
            LOGGER.warn("Left hand argument is null. Right side is "+extractable.name());
            if (!ctx.environment().getConfiguration().isStrictMode()) {
                return Undefined.UNDEFINED;
            }
            throw new CalculateException(String.format(pos + ": Impossible to access attribute/method '%s' on %s", extractable.name(), (left == null ? "null" : "undefined")));
        }
        ObjectExtractor extractor = new ObjectExtractor(ctx, left);
        try {
            return extractable.extract(ctx, extractor);
        } catch (ObjectExtractor.ExtractException e) {
            throw new CalculateException(e);
        }
    }
    
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
            Object calculatedLeft = null;
            try {
                calculatedLeft = left.calculate(context);
            } catch (CalculateException ex) {}

            if (calculatedLeft == UNDEFINED)
                calculatedLeft = null;
            return BinarySelectionOperator.this.render(context, pos, calculatedLeft, right);
        }
        
    }
    
}