/**
 * Copyright 2012 Lyncode
 *
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

import com.lyncode.builder.ListBuilder;
import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.functions.exceptions.FunctionNotFoundException;
import com.lyncode.jtwig.tree.api.Expression;

import java.util.ArrayList;
import java.util.List;

public class Composition implements Expression {
    private Expression expression;
    private List<FunctionElement> filters = new ArrayList<FunctionElement>();

    public Composition(Expression expression) {
        this.expression = expression;
    }

    public boolean add (Object functionElement) {
        if (functionElement instanceof FunctionElement)
            this.filters.add((FunctionElement) functionElement);
        else if (functionElement instanceof Variable)
            filters.add(new FunctionElement(((Variable) functionElement).getIdentifier()));
        else
            return false;
        return true;
    }

    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        try {
            Object resolved = expression.calculate(context);
            for (FunctionElement functionElement : filters) {
                List<Object> arguments = new ListBuilder<Object>()
                        .add(resolved)
                        .add(functionElement.getArguments().calculate(context).toArray())
                        .build();
                resolved = context.function(functionElement.getName()).execute(arguments.toArray());
            }
            return resolved;
        } catch (FunctionException e) {
            throw new CalculateException(e);
        } catch (FunctionNotFoundException e) {
            throw new CalculateException(e);
        }

    }
}
