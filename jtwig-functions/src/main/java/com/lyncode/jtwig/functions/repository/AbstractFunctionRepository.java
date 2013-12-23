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

package com.lyncode.jtwig.functions.repository;

import com.lyncode.jtwig.functions.Function;
import com.lyncode.jtwig.functions.exceptions.FunctionNotFoundException;
import com.lyncode.jtwig.functions.internal.cast.ToDouble;
import com.lyncode.jtwig.functions.internal.cast.ToInt;
import com.lyncode.jtwig.functions.internal.list.Concatenate;
import com.lyncode.jtwig.functions.internal.list.Join;
import com.lyncode.jtwig.functions.internal.math.Abs;
import com.lyncode.jtwig.functions.internal.string.Lower;
import com.lyncode.jtwig.functions.internal.string.Trim;
import com.lyncode.jtwig.functions.internal.string.Upper;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFunctionRepository {
    private Map<String, Function> functionMap = new HashMap<String, Function>();

    public AbstractFunctionRepository(FunctionDeclaration... functions) {
        addFunctions(
                // List functions
                new FunctionDeclaration(new Concatenate(), "concat", "concatenate"),
                new FunctionDeclaration(new Join(), "join"),

                // String function
                new FunctionDeclaration(new Upper(), "upper"),
                new FunctionDeclaration(new Lower(), "lower"),
                new FunctionDeclaration(new Trim(), "trim"),

                // Math functions
                new FunctionDeclaration(new Abs(), "abs"),

                // Cast functions
                new FunctionDeclaration(new ToDouble(), "toDouble", "toFloat"),
                new FunctionDeclaration(new ToInt(), "toInt")
        );
        addFunctions(functions);
    }

    private void addFunctions(FunctionDeclaration... functions) {
        for (FunctionDeclaration declaration : functions) {
            for (String alias : declaration.getAliases()) {
                functionMap.put(alias, declaration.getFunction());
            }
        }
    }

    public Function retrieve (String functionName) throws FunctionNotFoundException {
        if (!functionMap.containsKey(functionName)) throw new FunctionNotFoundException();
        return functionMap.get(functionName);
    }
}
