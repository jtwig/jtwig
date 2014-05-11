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

package com.lyncode.jtwig.expressions.operations.binary;

import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.expressions.api.BinaryOperation;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.expressions.model.FunctionElement;
import com.lyncode.jtwig.expressions.model.Variable;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;

public class CompositionOperation implements BinaryOperation {

    @Override
    public Object apply(RenderContext context, JtwigPosition position, Expression left, Expression right) throws CalculateException {
        FunctionElement.Compiled function;
        if (right instanceof Variable.Compiled)
            function = ((Variable.Compiled) right).toFunction();
        else if (right instanceof FunctionElement.Compiled)
            function = (FunctionElement.Compiled) right;
        else
            throw new CalculateException("Composition always requires a function to execute as the right argument");

        function.addFirstArgument(left);

        return function.calculate(context);
    }
}
