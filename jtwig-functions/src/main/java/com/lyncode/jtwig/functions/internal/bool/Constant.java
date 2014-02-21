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

package com.lyncode.jtwig.functions.internal.bool;

import com.lyncode.jtwig.functions.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.JtwigFunctionDeclaration;
import com.lyncode.jtwig.functions.exceptions.FunctionException;

import static com.lyncode.jtwig.functions.util.Requirements.requires;
import static org.hamcrest.CoreMatchers.*;

@JtwigFunctionDeclaration(name = "constant")
public class Constant implements JtwigFunction {
    @Override
    public Object execute(Object... arguments) throws FunctionException {
        requires(arguments)
                .withNumberOfArguments(equalTo(2))
                .withArgument(0, notNullValue())
                .withArgument(1, instanceOf(String.class));

        String constant = (String) arguments[1];
        int constantNamePosition = constant.lastIndexOf(".");
        String className = constant.substring(0, constantNamePosition);
        String constantName = constant.substring(constantNamePosition+1);

        try {
            return arguments[0].equals(Class.forName(className).getDeclaredField(constantName).get(null));
        } catch (Exception e) {
            throw new FunctionException(e);
        }
    }
}
