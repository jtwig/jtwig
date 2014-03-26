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
package com.lyncode.jtwig.functions.internal.list;

import com.lyncode.jtwig.functions.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.JtwigFunctionDeclaration;
import com.lyncode.jtwig.functions.exceptions.FunctionException;

import java.util.Collections;
import java.util.List;

import static com.lyncode.jtwig.functions.util.Requirements.requires;
import static org.hamcrest.CoreMatchers.equalTo;

@JtwigFunctionDeclaration(name = "sort")
public class Sort implements JtwigFunction {


    @Override
    public Object execute(Object... arguments) throws FunctionException {

        requires(arguments)
                .withNumberOfArguments(equalTo(1));

        List<Comparable> list;

        try {
            list = (List<Comparable>) arguments[0];
        } catch (ClassCastException e) {
            throw new FunctionException("First argument must be an instance of List");
        }

        Collections.sort(list);

        return list;
    }

}
