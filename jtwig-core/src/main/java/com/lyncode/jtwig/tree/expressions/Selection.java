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
import com.lyncode.jtwig.parser.positioning.Position;
import com.lyncode.jtwig.tree.api.AbstractExpression;
import com.lyncode.jtwig.tree.api.Expression;
import com.lyncode.jtwig.unit.util.ObjectExtractor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.lyncode.jtwig.types.Undefined.UNDEFINED;

public class Selection extends AbstractExpression {
    private List<Object> list = new ArrayList<>();

    public Selection(Position position) {
        super(position);
    }


    public boolean add (Object elem) {
        list.add(elem);
        return true;
    }


    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        Iterator<Object> iterator = list.iterator();
        if (!iterator.hasNext()) return UNDEFINED;

        Object expression = iterator.next();
        Object contextObject = resolve(expression, context);
        if (contextObject == null || contextObject == UNDEFINED)
            throw new CalculateException(getPosition()+": Value of "+expression+" is null or undefined");
        while (iterator.hasNext()) {
            ObjectExtractor extractor = new ObjectExtractor(contextObject);
            expression = iterator.next();
            if (expression instanceof Variable) {
                try {
                    contextObject = extractor.extract(((Variable) expression).getIdentifier());
                } catch (ObjectExtractor.ExtractException e) {
                    throw new CalculateException(((Variable) expression).getPosition()+": Unable to resolve value of "+expression, e);
                }
            } else if (expression instanceof FunctionElement) {
                Object[] arguments = ((FunctionElement) expression).arguments(context);
                try {
                    contextObject = extractor.extract(((FunctionElement) expression).getName(), arguments);
                } catch (ObjectExtractor.ExtractException e) {
                    throw new CalculateException(((FunctionElement) expression).getPosition()+": Unable to resolve value of "+expression, e);
                }
            }
        }
        return contextObject;
    }

    private Object resolve(Object obj, JtwigContext context) throws CalculateException {
        if (obj instanceof Expression)
            return ((Expression) obj).calculate(context);
        else
            return obj;
    }
}
