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
import com.lyncode.jtwig.functions.internal.date.DateFormat;
import com.lyncode.jtwig.functions.internal.date.DateModify;
import com.lyncode.jtwig.functions.internal.generic.*;
import com.lyncode.jtwig.functions.internal.list.*;
import com.lyncode.jtwig.functions.internal.map.MapKeys;
import com.lyncode.jtwig.functions.internal.math.Abs;
import com.lyncode.jtwig.functions.internal.math.JNumberFormat;
import com.lyncode.jtwig.functions.internal.math.Round;
import com.lyncode.jtwig.functions.internal.string.*;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFunctionRepository {
    private Map<String, Function> functionMap = new HashMap<String, Function>();

    public AbstractFunctionRepository(FunctionDeclaration... functions) {
        addFunctions(
                // List functions
                new FunctionDeclaration(new Concatenate(), "concat", "concatenate"),
                new FunctionDeclaration(new Join(), "join"),
                new FunctionDeclaration(new Batch(), "batch"),
                new FunctionDeclaration(new Merge(), "merge"),
                new FunctionDeclaration(new Slice(), "slice"),
                new FunctionDeclaration(new MapKeys(), "keys"),

                // String function
                new FunctionDeclaration(new Upper(), "upper"),
                new FunctionDeclaration(new Lower(), "lower"),
                new FunctionDeclaration(new StripTags(), "striptags"),
                new FunctionDeclaration(new Trim(), "trim"),
                new FunctionDeclaration(new Split(), "split"),
                new FunctionDeclaration(new Capitalize(), "capitalize"),
                new FunctionDeclaration(new Title(), "title"),
                new FunctionDeclaration(new Replace(), "replace"),
                new FunctionDeclaration(new Format(), "format"),
                new FunctionDeclaration(new Nl2Br(), "nl2br"),
                new FunctionDeclaration(new UrlEncode(), "url_encode"),
                new FunctionDeclaration(new Escape(), "escape", "e"),

                // Date Functions
                new FunctionDeclaration(new DateModify(), "date_modify"),
                new FunctionDeclaration(new DateFormat(), "date", "date_format"),
                new FunctionDeclaration(new ConvertEncoding(), "convert_encoding"),

                // Math functions
                new FunctionDeclaration(new Abs(), "abs"),
                new FunctionDeclaration(new JNumberFormat(), "number_format"),
                new FunctionDeclaration(new Round(), "round"),

                // Cast functions
                new FunctionDeclaration(new ToDouble(), "toDouble", "toFloat"),
                new FunctionDeclaration(new ToInt(), "toInt"),

                // Generic functions
                new FunctionDeclaration(new Length(), "length"),
                new FunctionDeclaration(new Default(), "default"),
                new FunctionDeclaration(new First(), "first"),
                new FunctionDeclaration(new Last(), "last"),
                new FunctionDeclaration(new JsonEncode(), "json_encode"),
                new FunctionDeclaration(new Reverse(), "reverse")
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
