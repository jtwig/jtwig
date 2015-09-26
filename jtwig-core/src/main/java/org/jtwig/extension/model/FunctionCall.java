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
import org.jtwig.content.api.ability.ExecutionAware;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.extension.api.functions.Function;
import org.jtwig.functions.exceptions.FunctionException;
import org.jtwig.functions.exceptions.FunctionNotFoundException;
import static org.jtwig.functions.parameters.input.InputParameters.parameters;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;

public class FunctionCall extends Callable {

    public FunctionCall(JtwigPosition position, String name) {
        super(position, name);
    }

    @Override
    public Expression compile(CompileContext context) throws CompileException {
        // Delay checking for known functions, because we also deal with Macro
        // calls here
        List<Expression> compiledArguments = new ArrayList<>();
        for (CompilableExpression argument : arguments) {
            compiledArguments.add(argument.compile(context));
        }
        return new FunctionCall.Compiled(position(), name, compiledArguments);
    }
    
    public static class Compiled extends Callable.Compiled {

        public Compiled(JtwigPosition position, String name, List<Expression> arguments) {
            super(position, name, arguments);
        }

        @Override
        public Object calculate(RenderContext context) throws CalculateException {
            try {
                if (context.map(name) instanceof ExecutionAware) {
                    return ((ExecutionAware)context.map(name)).execute(context, null, calculateArguments(context));
                }
                
                Function function = context.environment().getConfiguration().getExtensions().getFunction(name);
                if (function != null) {
                    return function.evaluate(context.environment(), context, calculateArguments(context));
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
        
    }
    
}
