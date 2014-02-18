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

package com.lyncode.jtwig.functions.internal.math;

import com.lyncode.jtwig.functions.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.JtwigFunctionDeclaration;
import com.lyncode.jtwig.functions.exceptions.FunctionException;

import static com.lyncode.jtwig.functions.util.Requirements.requires;
import static java.lang.Math.abs;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;

@JtwigFunctionDeclaration(name = "abs")
public class Abs implements JtwigFunction {
    @Override
    public Object execute(Object... arguments) throws FunctionException {
        requires(arguments)
                .withNumberOfArguments(equalTo(1))
                .withArgument(0, instanceOf(Number.class));

        if (arguments[0] instanceof Double)
            return abs((Double) arguments[0]);
        else if (arguments[0] instanceof Float)
            return abs((Float) arguments[0]);
        else if (arguments[0] instanceof Integer)
            return abs((Integer) arguments[0]);
        else if (arguments[0] instanceof Long)
            return abs((Long) arguments[0]);
        else
            return arguments[0];
    }
}
