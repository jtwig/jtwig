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
import org.jtwig.extension.api.filters.Filter;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;

public class FilterCall extends Callable {
    public FilterCall(final JtwigPosition position, final String name) {
        super(position, name);
    }

    @Override
    public Expression compile(CompileContext context) throws CompileException {
        if (context.environment().getConfiguration().getExtensions().getFilter(name) == null) {
            throw new CompileException("Unable to find filter '"+name+"'");
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
        public Object calculate(RenderContext context) throws CalculateException {
            Filter filter = context.environment().getConfiguration().getExtensions().getFilter(name);
            if (filter == null) {
                throw new CalculateException("Unable to find filter '"+name+"'");
            }

            return filter.evaluate(context.environment(), context, left.calculate(context), calculateArguments(context));
        }

        public Compiled cloneAndAddLeftArgument (final Expression left) {
            return new Compiled(position(), name, arguments).withLeft(left);
        }
        
        public Compiled withLeft(final Expression left) {
            this.left = left;
            return this;
        }
        
        public Compiled passLeft(final Expression left) {
            if (this.left instanceof FilterCall.Compiled) {
                ((FilterCall.Compiled)this.left).passLeft(left);
            } else {
                this.left = left;
            }
            return this;
        }
    }
}
