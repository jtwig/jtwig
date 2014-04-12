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

package com.lyncode.jtwig.tree.expressions;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.functions.exceptions.FunctionNotFoundException;
import com.lyncode.jtwig.functions.parameters.GivenParameters;
import com.lyncode.jtwig.parser.positioning.Position;
import com.lyncode.jtwig.tree.api.AbstractExpression;
import com.lyncode.jtwig.tree.api.Expression;

import java.util.ArrayList;
import java.util.List;

public class FunctionElement extends AbstractExpression {
    private String name;
    private List<Expression> arguments = new ArrayList<>();

    public FunctionElement(Position position, String name, Expression argument) {
        super(position);
        this.name = name;
        arguments.add(argument);
    }

    public FunctionElement(Position position, String name) {
        super(position);
        this.name = name;
    }

    public FunctionElement add(Expression argument) {
        arguments.add(argument);
        return this;
    }

    public String getName() {
        return name;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        try {
            GivenParameters parameters = new GivenParameters();
            for (Object obj : arguments(context))
                parameters.addObject(obj);
            try {
                return context.executeFunction(getName(), parameters);
            } catch (FunctionNotFoundException e) {
                throw new CalculateException(getPosition()+": "+e.getMessage(), e);
            }
        } catch (FunctionException e) {
            throw new CalculateException(e);
        }
    }

    public Object[] arguments(JtwigContext context) throws CalculateException {
        return calculateArguments(context);
    }

    private Object[] calculateArguments(JtwigContext context) throws CalculateException {
        List<Object> result = new ArrayList<>();
        for (Expression argument : arguments) {
            result.add(argument.calculate(context));
        }
        return result.toArray();
    }

    public FunctionElement addArgument(int position, Expression expression) {
        arguments.add(position, expression);
        return this;
    }

    @Override
    public String toString() {
        return "Method or Field with name '" + name + "'";
    }
}
