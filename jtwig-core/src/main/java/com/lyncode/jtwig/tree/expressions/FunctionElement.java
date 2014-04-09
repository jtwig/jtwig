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
import com.lyncode.jtwig.functions.parameters.GivenParameters;
import com.lyncode.jtwig.tree.api.Element;
import com.lyncode.jtwig.tree.api.Expression;

public class FunctionElement implements Element, Expression {
    private String name;
    private ValueList arguments;

    public FunctionElement(String name, Expression argument) {
        this.name = name;
        arguments = new ValueList();
        arguments.add(argument);
    }

    public FunctionElement(String name) {
        this.name = name;
        arguments = new ValueList();
    }

    public boolean add(Expression argument) {
        return arguments.add(argument);
    }

    public String getName() {
        return name;
    }

    public ValueList getArguments() {
        return arguments;
    }

    public String toString () {
        return name+arguments;
    }

    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        try {
            GivenParameters parameters = new GivenParameters();
            for (Object obj : arguments(context))
                parameters.addObject(obj);
            return context.executeFunction(getName(), parameters);
        } catch (FunctionException e) {
            throw new CalculateException(e);
        }
    }

    private Object[] arguments(JtwigContext context) throws CalculateException {
        return arguments.calculate(context).toArray();
    }
}
