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

package com.lyncode.jtwig.functions.repository;

import com.lyncode.jtwig.functions.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.JtwigFunctionDeclaration;
import com.lyncode.jtwig.functions.exceptions.FunctionNotFoundException;
import com.lyncode.jtwig.functions.internal.bool.*;
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
    private Map<String, JtwigFunction> functionMap = new HashMap<String, JtwigFunction>();

    public AbstractFunctionRepository(JtwigFunction... jtwigFunctions) {
        add(
                // List functions
                new Concatenate(),
                new Join(),
                new Batch(),
                new Merge(),
                new Slice(),
                new MapKeys(),
                new Sort(),

                // String function
                new Upper(),
                new Lower(),
                new StripTags(),
                new Trim(),
                new Split(),
                new Capitalize(),
                new Title(),
                new Replace(),
                new Format(),
                new Nl2Br(),
                new UrlEncode(),
                new Escape(),

                // Date Functions
                new DateModify(),
                new DateFormat(),
                new ConvertEncoding(),

                // Math functions
                new Abs(),
                new JNumberFormat(),
                new Round(),

                // Cast functions
                new ToDouble(),
                new ToInt(),

                // Generic functions
                new Length(),
                new Default(),
                new First(),
                new Last(),
                new JsonEncode(),
                new Reverse(),
                new Range(),

                // Boolean functions
                new Empty(),
                new Constant(),
                new Defined(),
                new Even(),
                new Odd(),
                new IsIterable(),
                new IsNull(),
                new DivisableBy()
        );
        add(jtwigFunctions);
    }

    public void add(JtwigFunction... jtwigFunctions) {
        for (JtwigFunction jtwigFunction : jtwigFunctions)
            add(jtwigFunction);
    }

    public void add (JtwigFunction jtwigFunction) {
        Class<? extends JtwigFunction> functionClass = jtwigFunction.getClass();
        if (functionClass.isAnnotationPresent(JtwigFunctionDeclaration.class)) {
            JtwigFunctionDeclaration declaration = functionClass.getAnnotation(JtwigFunctionDeclaration.class);
            add(jtwigFunction, getName(declaration, functionClass), declaration.aliases());
        }
        else {
            add(jtwigFunction, getNameFromClass(functionClass));
        }
    }

    private String getName(JtwigFunctionDeclaration declaration, Class<? extends JtwigFunction> functionClass) {
        if (!declaration.name().equals(""))
            return declaration.name();
        else
            return getNameFromClass(functionClass);
    }

    private String getNameFromClass(Class<? extends JtwigFunction> functionClass) {
        String functionName = functionClass.getSimpleName();
        if (functionName == null || functionName.equals(""))
            throw new RuntimeException("Function without name, try to use add(function, name) method");
        return functionName.substring(0, 1).toLowerCase() + functionName.substring(1);
    }

    public void add(JtwigFunction jtwigFunction, String name, String... aliases) {
        functionMap.put(name, jtwigFunction);
        for (String alias : aliases) {
            functionMap.put(alias, jtwigFunction);
        }
    }

    public JtwigFunction retrieve (String functionName) throws FunctionNotFoundException {
        if (!functionMap.containsKey(functionName)) throw new FunctionNotFoundException();
        return functionMap.get(functionName);
    }
}
