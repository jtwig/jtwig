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

package org.jtwig.extension.model;

import java.util.ArrayList;
import java.util.List;
import org.jtwig.compile.CompileContext;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.extension.api.test.Test;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.jtwig.types.Undefined;

public class TestCall extends Callable {
    public TestCall(final JtwigPosition position, final String name) {
        super(position, name);
    }

    @Override
    public Expression compile(CompileContext context) throws CompileException {
        if (context.environment().getConfiguration().getExtensions().getTest(name) == null) {
            throw new CompileException("Unable to find test '"+name+"'");
        }
        
        List<Expression> compiledArguments = new ArrayList<>();
        for (CompilableExpression argument : arguments) {
            compiledArguments.add(argument.compile(context));
        }
        return new Compiled(position(), name, compiledArguments);
    }

    public static class Compiled extends Callable.Compiled {
        
        private Expression left;

        public Compiled(JtwigPosition position, String name, List<Expression> arguments) {
            super(position, name, arguments);
        }

        @Override
        public Boolean calculate(RenderContext context) throws CalculateException {
            Test test = context.environment().getConfiguration().getExtensions().getTest(name);
            if (test == null) {
                throw new CalculateException("Unable to find test '"+name+"'");
            }
            Object calculatedLeft = left.calculate(context);
            if (!test.acceptUndefinedArguments() && calculatedLeft == Undefined.UNDEFINED)  {
                calculatedLeft = null;
            }
            
            return test.evaluate(calculatedLeft, calculateArguments(context));
        }

        public Compiled cloneAndAddLeftArgument (final Expression left) {
            return new Compiled(position(), name, arguments).withLeft(left);
        }
        
        public Compiled withLeft(final Expression left) {
            this.left = left;
            return this;
        }
    }
}
