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
import org.jtwig.render.RenderContext;
import static org.jtwig.types.Undefined.UNDEFINED;

public abstract class UnaryOperator extends AbstractOperator {
    public UnaryOperator(final String name, final int precedence) {
        super(name, precedence);
    }

    @Override
    public Expression compile(final Environment env, final JtwigPosition pos,
            final CompileContext ctx, final Object... args)
            throws CompileException {
        assert args.length == 1;
        
        final Expression input = (Expression)args[0];
        return new UnaryCallback(pos, input);
    }
    
    public abstract Object render(RenderContext ctx, JtwigPosition pos, Object input) throws CalculateException;
    
    public class UnaryCallback implements Expression {
        protected final JtwigPosition pos;
        protected final Expression input;
        
        public UnaryCallback(final JtwigPosition pos, final Expression input) {
            this.pos = pos;
            this.input = input;
        }

        @Override
        public Object calculate(final RenderContext context) throws CalculateException {
            Object calculatedInput = input.calculate(context);

            if (calculatedInput == UNDEFINED)
                calculatedInput = null;
            return UnaryOperator.this.render(context, pos, calculatedInput);
        }
        
    }
    
}