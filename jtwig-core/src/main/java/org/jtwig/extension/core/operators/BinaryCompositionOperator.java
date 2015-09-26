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
import org.jtwig.exception.CompileException;
import org.jtwig.expressions.api.Expression;
import org.jtwig.extension.api.operator.BinaryOperator;
import org.jtwig.extension.model.FilterCall;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.render.RenderContext;
import org.parboiled.Rule;

/**
 * The composition operator is used to apply filters to left-hand arguments,
 * including chaining filters.
 */
public class BinaryCompositionOperator extends BinaryOperator {

    public BinaryCompositionOperator(String name, int precedence) {
        super(name, precedence);
    }

    @Override
    public Expression compile(Environment env, JtwigPosition pos, CompileContext ctx, Object... args) throws CompileException {
        assert args.length == 2;

        final Expression left = (Expression)args[0];
        final Expression right = (Expression)args[1];
        
        if (right instanceof FilterCall.Compiled) {
            return ((FilterCall.Compiled)right).cloneAndAddLeftArgument(left);
        }
        throw new CompileException(pos + ": Composition always requires a filter to execute as the right argument");
    }

    @Override
    public Rule getRightSideRule(JtwigExpressionParser expr, Environment env) {
        return expr.callable(FilterCall.class);
    }

    @Override
    public Object render(RenderContext ctx, JtwigPosition pos, Object left, Object right) {
        throw new UnsupportedOperationException();
    }
    
}