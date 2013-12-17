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

package com.lyncode.jtwig.tree.value;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.tree.api.Calculable;
import com.lyncode.jtwig.tree.helper.ElementList;
import com.lyncode.jtwig.util.ObjectExtractor;

import java.util.List;

public class Composition extends ElementList implements Calculable {
    public Composition(Object... list) {
        super(list);
    }

    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        Object contextObject = null;
        for (Object obj : getList()) {
            if (contextObject == null) contextObject = resolve(obj, context);
            else {
                ObjectExtractor objectExtractor = new ObjectExtractor(contextObject);
                if (obj instanceof Variable) {
                    try {
                        contextObject = objectExtractor.extract(((Variable) obj).getIdentifier());
                    } catch (ObjectExtractor.ExtractException e) {
                        throw new CalculateException(e);
                    }
                } else if (obj instanceof FunctionElement) {
                    try {
                        List<?> arguments = (List<?>) ((FunctionElement) obj).getArguments().calculate(context);
                        contextObject = objectExtractor.extract(((FunctionElement) obj).getName(), arguments.toArray());
                    } catch (ObjectExtractor.ExtractException e) {
                        throw new CalculateException(e);
                    }
                }
            }
        }
        return contextObject;
    }

    private Object resolve(Object obj, JtwigContext context) throws CalculateException {
        if (obj instanceof Calculable)
            return ((Calculable) obj).calculate(context);
        else
            return obj;
    }
}
