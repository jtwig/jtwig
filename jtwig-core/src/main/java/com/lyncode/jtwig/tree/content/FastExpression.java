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

package com.lyncode.jtwig.tree.content;

import com.lyncode.builder.ListBuilder;
import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.functions.exceptions.FunctionNotFoundException;
import com.lyncode.jtwig.tree.api.Renderable;
import com.lyncode.jtwig.tree.value.FunctionElement;
import com.lyncode.jtwig.tree.value.Variable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FastExpression implements Renderable {
    private Object expression;
    private List<FunctionElement> filters = new ArrayList<FunctionElement>();

    public FastExpression (Object expression) {
        this.expression = expression;
    }

    public boolean add (Object object) {
        if (object instanceof Variable)
            filters.add(new FunctionElement(((Variable) object).getIdentifier()));
        else if (object instanceof FunctionElement)
            filters.add((FunctionElement) object);

        return true;
    }

    public Object getExpression() {
        return expression;
    }

    public List<FunctionElement> getFilters() {
        return filters;
    }

    @Override
    public boolean render(OutputStream outputStream, JtwigContext context) throws RenderException {
        try {
            Object resolved = context.resolve(expression);
            for (FunctionElement functionElement : filters) {
                List<Object> arguments = new ListBuilder<Object>()
                        .add(resolved)
                        .add(((List<Object>) context.resolve(functionElement.getArguments())).toArray())
                        .build();
                resolved = context.function(functionElement.getName()).execute(arguments.toArray());
            }
            outputStream.write(String.valueOf(resolved).getBytes());
            return true;
        } catch (IOException e) {
            throw new RenderException(e);
        } catch (CalculateException e) {
            throw new RenderException(e);
        } catch (FunctionException e) {
            throw new RenderException(e);
        } catch (FunctionNotFoundException e) {
            throw new RenderException(e);
        }
    }

    public String toString () {
        return "Render the result of "+expression;
    }
}
