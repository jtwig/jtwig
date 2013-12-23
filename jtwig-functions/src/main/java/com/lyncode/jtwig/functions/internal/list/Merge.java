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

package com.lyncode.jtwig.functions.internal.list;

import com.lyncode.jtwig.functions.Function;
import com.lyncode.jtwig.functions.exceptions.FunctionException;

import java.util.*;

public class Merge implements Function {
    @Override
    public Object execute(Object... arguments) throws FunctionException {
        if (arguments.length < 2) throw new FunctionException("Invalid number of arguments. Expecting at least two arguments.");
        if (arguments[0] instanceof Iterable)
            return mergeList(arguments);
        else if (arguments[0] instanceof Map)
            return mergeMap(arguments);
        else if (arguments[0].getClass().isArray())
            return mergeArray(arguments);
        else
            throw new FunctionException("Invalid arguments. Expecting lists, arrays or maps values.");
    }

    private Object mergeArray(Object... arguments) {
        List<Object> result = new ArrayList<Object>();
        for (Object obj : arguments) {
            if (obj == null) continue;
            Object[] list = (Object[]) obj;
            for (Object value : list) {
                result.add(value);
            }
        }
        return result.toArray();
    }

    private Object mergeMap(Object... arguments) {
        Map<Object, Object> result;
        if (arguments[0] instanceof TreeMap)
            result = new TreeMap<Object, Object>();
        else
            result = new HashMap<Object, Object>();
        for (Object obj : arguments) {
            if (obj == null) continue;
            result.putAll((Map) obj);
        }
        return result;
    }

    private Object mergeList(Object... arguments) {
        List<Object> result = new ArrayList<Object>();
        for (Object obj : arguments) {
            if (obj == null) continue;
            result.addAll((List) obj);
        }
        return result;
    }
}
