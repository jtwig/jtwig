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

public class Composition extends AbstractExpression {
    private final Expression expression;
    private List<FunctionElement> filters = new ArrayList<FunctionElement>();

    public Composition(Position position, Expression expression) {
        super(position);
        this.expression = expression;
    }

    public boolean add (Object functionElement) {
        if (functionElement instanceof FunctionElement)
            this.filters.add((FunctionElement) functionElement);
        else if (functionElement instanceof Variable) {
            Variable variable = (Variable) functionElement;
            filters.add(new FunctionElement(variable.getPosition(), variable.getIdentifier()));
        }
        else
            return false;
        return true;
    }

    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        try {
            Object resolved = expression.calculate(context);
            for (FunctionElement functionElement : filters) {
                GivenParameters parameters = new GivenParameters()
                        .addObject(resolved)
                        .addArray(functionElement.arguments(context));

                try {
                    resolved = context.executeFunction(functionElement.getName(), parameters);
                } catch (FunctionNotFoundException e) {
                    throw new CalculateException(functionElement.getPosition()+": "+e.getMessage(), e);
                }
            }
            return resolved;
        } catch (FunctionException e) {
            throw new CalculateException(getPosition()+": "+e.getMessage(), e);
        }
    }
}
