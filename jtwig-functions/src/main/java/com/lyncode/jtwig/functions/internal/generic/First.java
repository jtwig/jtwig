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

package com.lyncode.jtwig.functions.internal.generic;

import com.lyncode.jtwig.functions.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.JtwigFunctionDeclaration;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.functions.util.ObjectIterator;

import java.util.Iterator;
import java.util.Map;

import static com.lyncode.jtwig.functions.util.Requirements.requires;
import static org.hamcrest.CoreMatchers.equalTo;

@JtwigFunctionDeclaration(name = "first")
public class First implements JtwigFunction {

    @Override
    public Object execute(Object... arguments) throws FunctionException {
        requires(arguments)
                .withNumberOfArguments(equalTo(1));

        if ((arguments[0] instanceof Iterable) || arguments[0].getClass().isArray()) {
            ObjectIterator objectIterator = new ObjectIterator(arguments[0]);
            if (objectIterator.hasNext()) return objectIterator.next();
            else return null;
        }
        else if (arguments[0] instanceof Map) {
            Iterator iterator = ((Map) arguments[0]).keySet().iterator();
            if (iterator.hasNext())
                return ((Map) arguments[0]).get(iterator.next());
            else
                return null;
        }
        else if (arguments[0] instanceof String)
            return ((String) arguments[0]).charAt(0);
        else return arguments[0];
    }
}
