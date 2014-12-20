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
import org.jtwig.exception.RenderException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.functions.exceptions.FunctionException;
import org.jtwig.functions.exceptions.FunctionNotFoundException;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.jtwig.util.ObjectExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jtwig.content.api.ability.ExecutionAware;

import static org.jtwig.functions.parameters.input.InputParameters.parameters;

public class FunctionElement extends AbstractCompilableExpression {
    private final String name;
    private final List<CompilableExpression> arguments = new ArrayList<>();

    public FunctionElement(JtwigPosition position, String name) {
        super(position);
        this.name = name;
    }

    public FunctionElement add(CompilableExpression argument) {
        arguments.add(argument);
        return this;
    }

    @Override
    public Expression compile(CompileContext context) throws CompileException {
        List<Expression> compiledArguments = new ArrayList<>();
        for (CompilableExpression argument : arguments) {
            compiledArguments.add(argument.compile(context));
        }
        return new Compiled(position(), name, compiledArguments);
    }

    public static class Compiled implements Expression {
        private final JtwigPosition position;
        private final String name;
        private final List<Expression> arguments;

        public Compiled(JtwigPosition position, String name, List<Expression> arguments) {
            this.position = position;
            this.name = name;
            this.arguments = new CopyOnWriteArrayList<>(arguments);
        }

        private Object[] calculateArguments(RenderContext context) throws CalculateException {
            List<Object> result = new ArrayList<>();
            for (Expression argument : arguments) {
                result.add(argument.calculate(context));
            }
            return result.toArray();
        }

        @Override
        public Object calculate(RenderContext context) throws CalculateException {
            try {
                if (context.map(name) instanceof ExecutionAware) {
                    return ((ExecutionAware)context.map(name)).execute(context, null, calculateArguments(context));
                }
                
                try {
                    return context.executeFunction(name, parameters(calculateArguments(context)));
                } catch (FunctionNotFoundException e) {
                    throw new CalculateException(position + ": " + e.getMessage(), e);
                }
            } catch (FunctionException | RenderException e) {
                throw new CalculateException(e);
            }
        }

        public Compiled cloneAndAddArgument (Expression expression) {
            List<Expression> arguments = new ArrayList<>(this.arguments);
            arguments.add(0, expression);
            return new Compiled(position(), name, arguments);
        }

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
