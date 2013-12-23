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

package com.lyncode.jtwig.functions.internal.generic;

import com.lyncode.jtwig.functions.Function;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.functions.util.ObjectIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reverse implements Function {
    @Override
    public Object execute(Object... arguments) throws FunctionException {
        if (arguments.length != 1) throw new FunctionException("Invalid number of arguments");
        if ((arguments[0] instanceof Iterable) || arguments[0].getClass().isArray())
            return reverse(new ObjectIterator(arguments[0]), arguments[0] instanceof Iterable);
        else if (arguments[0] instanceof String)
            return new StringBuilder(((String) arguments[0])).reverse().toString();
        else return 0;
    }

    private Object reverse(ObjectIterator objectIterator, boolean list) {
        List<Object> result = new ArrayList<Object>();
        while (objectIterator.hasNext())
            result.add(objectIterator.next());

        Collections.reverse(result);
        if (list) return result;
        else return result.toArray();
    }
}
