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

package com.lyncode.jtwig.functions.internal.string;

import com.lyncode.jtwig.functions.Function;
import com.lyncode.jtwig.functions.exceptions.FunctionException;

import java.util.Map;

public class Replace implements Function {
    @Override
    public Object execute(Object... arguments) throws FunctionException {
        if (arguments.length != 2) throw new FunctionException("Invalid number of arguments");
        if (!(arguments[1] instanceof Map)) throw new FunctionException("Invalid 2nd argument. Must be a map.");
        if (arguments[0] == null) return null;
        String input = arguments[0].toString();
        Map<String, Object> replacements = (Map<String, Object>) arguments[1];
        for (String key : replacements.keySet())
            if (replacements.get(key) != null)
                input = input.replace(key, replacements.get(key).toString());
        return input;
    }
}
