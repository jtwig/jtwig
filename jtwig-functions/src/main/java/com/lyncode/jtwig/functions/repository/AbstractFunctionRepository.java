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
import com.lyncode.jtwig.functions.internal.string.Concatenate;
import com.lyncode.jtwig.functions.internal.string.Join;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFunctionRepository {
    private Map<String, Function> functionMap = new HashMap<String, Function>();

    public AbstractFunctionRepository(FunctionDeclaration... functions) {
        addFunctions(
                new FunctionDeclaration(new Concatenate(), "concat", "concatenate"),
                new FunctionDeclaration(new Join(), "join")
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
