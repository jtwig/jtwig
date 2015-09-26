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
import org.jtwig.extension.model.TestCall;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;

public class BinaryIsNotOperator extends BinaryIsOperator {
    public BinaryIsNotOperator(String name, int precedence) {
        super(name, precedence);
    }

    @Override
    public Expression compile(Environment env, JtwigPosition pos, CompileContext ctx, Object... args) throws CompileException {
        final Expression left = (Expression)args[0];
        final Expression right = (Expression)args[1];
        
        return new Expression() {

            @Override
            public Object calculate(RenderContext context) throws CalculateException {
                return !((TestCall.Compiled)right).withLeft(left).calculate(context);
            }
        };
    }
}