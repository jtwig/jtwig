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

package com.lyncode.jtwig.expressions.model;

import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.functions.exceptions.FunctionNotFoundException;
import com.lyncode.jtwig.functions.parameters.GivenParameters;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.util.ObjectExtractor;

import java.util.ArrayList;
import java.util.List;

public class FunctionElement extends AbstractCompilableExpression {
    private String name;
    private List<CompilableExpression> arguments = new ArrayList<>();

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
            this.arguments = arguments;
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
                GivenParameters parameters = new GivenParameters()
                        .add(calculateArguments(context));

                try {
                    return context.model().executeFunction(name, parameters);
                } catch (FunctionNotFoundException e) {
                    throw new CalculateException(position + ": " + e.getMessage(), e);
                }
            } catch (FunctionException e) {
                throw new CalculateException(e);
            }
        }

        public Compiled addFirstArgument(Expression expression) {
            this.arguments.add(0, expression);
            return this;
        }

        public Object extract(RenderContext context, ObjectExtractor extractor) throws CalculateException, ObjectExtractor.ExtractException {
            return extractor.extract(name, calculateArguments(context));
        }

        public JtwigPosition position() {
            return position;
        }

        public String name() {
            return name;
        }
    }
}
