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
package com.lyncode.jtwig.functions.internal.generic;

import com.lyncode.jtwig.functions.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.JtwigFunctionDeclaration;
import com.lyncode.jtwig.functions.exceptions.FunctionException;

import java.util.ArrayList;
import java.util.List;

import static com.lyncode.jtwig.functions.util.Requirements.between;
import static com.lyncode.jtwig.functions.util.Requirements.requires;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Created with IntelliJ IDEA.
 * User: Vitali Carbivnicii
 * Date: 2014-03-25
 * Time: 23:22
 */
@JtwigFunctionDeclaration(name = "range")
public class Range implements JtwigFunction {

    @Override
    public Object execute(Object... arguments) throws FunctionException {

        requires(arguments)
                .withNumberOfArguments(between(2, 3))
                .withArgument(0, instanceOf(Integer.class))
                .withArgument(1, instanceOf(Integer.class))
        ;

        int start = (int) arguments[0];
        int end = (int) arguments[1];
        int step = 1;

        if (arguments.length == 3) {

            requires(arguments)
                    .withArgument(2, instanceOf(Integer.class))
            ;

            step = (int) arguments[2];

            if (step == 0) {
                throw new FunctionException("Step must not be 0");
            }
        }

        // reverse mode
        if (start > end) {
            // negate step for reversed mode, if positive given
            if (step > 0) step = -step;

            if (step < end) throw new FunctionException("Step is too big");
        } else {
            if (step > end) throw new FunctionException("Step is too big");
        }


        List<Integer> result = new ArrayList<>();

        for (int i = start; (step > 0) ? i <= end : i >= end; i += step) {
            result.add(i);
        }

        return result;
    }


}
