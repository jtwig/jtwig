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

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class FunctionDeclaration {
    private List<String> aliases;
    private Function function;

    public FunctionDeclaration (Function function, String name, String... aliases) {
        this.function = function;
        this.aliases = new ArrayList<String>();
        this.aliases.add(name);
        this.aliases.addAll(asList(aliases));
    }

    public List<String> getAliases() {
        return aliases;
    }

    public Function getFunction() {
        return function;
    }
}
