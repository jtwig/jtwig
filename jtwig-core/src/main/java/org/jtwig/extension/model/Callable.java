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
import java.util.concurrent.CopyOnWriteArrayList;
import org.jtwig.exception.CalculateException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.expressions.api.Extractable;
import org.jtwig.expressions.model.AbstractCompilableExpression;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.jtwig.util.ObjectExtractor;

public abstract class Callable extends AbstractCompilableExpression {
    protected final String name;
    protected final List<CompilableExpression> arguments = new ArrayList<>();

    public Callable(final JtwigPosition position, final String name) {
        super(position);
        this.name = name;
    }

    public Callable add(final CompilableExpression argument) {
        arguments.add(argument);
        return this;
    }

//    @Override
//    public Expression compile(final CompileContext context)
//            throws CompileException {
//        List<Expression> compiledArguments = new ArrayList<>();
//        for (CompilableExpression argument : arguments) {
//            compiledArguments.add(argument.compile(context));
//        }
//        return new Compiled(position(), name, compiledArguments);
//    }

    public static abstract class Compiled implements Expression, Extractable {
        protected final JtwigPosition position;
        protected final String name;
        protected final List<Expression> arguments;

        public Compiled(final JtwigPosition position, final String name,
                final List<Expression> arguments) {
            this.position = position;
            this.name = name;
            this.arguments = new CopyOnWriteArrayList<>(arguments);
        }

        protected Object[] calculateArguments(final RenderContext context)
                throws CalculateException {
            List<Object> result = new ArrayList<>();
            for (Expression argument : arguments) {
                result.add(argument.calculate(context));
            }
            return result.toArray();
        }

//        @Override
//        public Object calculate(final RenderContext context) throws CalculateException {
//            try {
//                if (context.map(name) instanceof ExecutionAware) {
//                    return ((ExecutionAware)context.map(name)).execute(context, null, calculateArguments(context));
//                }
//                
//                try {
//                    return context.executeFunction(name, parameters(calculateArguments(context)));
//                } catch (FunctionNotFoundException e) {
//                    throw new CalculateException(position + ": " + e.getMessage(), e);
//                }
//            } catch (FunctionException | RenderException e) {
//                throw new CalculateException(e);
//            }
//        }
//
//        public Compiled cloneAndAddArgument (Expression expression) {
//            List<Expression> arguments = new ArrayList<>(this.arguments);
//            arguments.add(0, expression);
//            return new Compiled(position(), name, arguments);
//        }

        public Object extract(RenderContext context, ObjectExtractor extractor) throws CalculateException, ObjectExtractor.ExtractException {
            return extractor.extract(name, calculateArguments(context));
        }

        public Expression getFirstArgument() {
            return this.arguments.get(0);
        }

        public JtwigPosition position() {
            return position;
        }

        public String name() {
            return name;
        }

        public boolean hasArguments() {
            return !this.arguments.isEmpty();
        }
    }
}
