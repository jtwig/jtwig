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
import com.lyncode.jtwig.types.Undefined;
import com.lyncode.jtwig.util.ObjectExtractor;

public class SelectionOperation implements BinaryOperation {
    @Override
    public Object apply(RenderContext context, JtwigPosition position, Expression left, Expression right) throws CalculateException {
        Object calculate = left.calculate(context);
        if (calculate == null) {
            if (context.configuration().strictMode()) {
                if (right instanceof Variable.Compiled) {
                    String propertyName = ((Variable.Compiled) right).name();
                    throw new CalculateException(String.format(position + ": Impossible to access attribute/method '%s' on null", propertyName));
                } else if (right instanceof FunctionElement.Compiled) {
                    String propertyName = ((FunctionElement.Compiled) right).name();
                    throw new CalculateException(String.format(position + ": Impossible to access attribute/method '%s' on null", propertyName));
                }
            } else return Undefined.UNDEFINED;
        } else if (calculate == Undefined.UNDEFINED) {
            if (context.configuration().strictMode()) {
                if (right instanceof Variable.Compiled) {
                    String propertyName = ((Variable.Compiled) right).name();
                    throw new CalculateException(String.format(position + ": Impossible to access attribute/method '%s' on undefined", propertyName));
                } else if (right instanceof FunctionElement.Compiled) {
                    String propertyName = ((FunctionElement.Compiled) right).name();
                    throw new CalculateException(String.format(position + ": Impossible to access attribute/method '%s' on undefined", propertyName));
                }
            } else return Undefined.UNDEFINED;
        }
        ObjectExtractor extractor = new ObjectExtractor(context, calculate);
        try {
            if (right instanceof Variable.Compiled)
                return ((Variable.Compiled) right).extract(extractor);
            else if (right instanceof FunctionElement.Compiled)
                return ((FunctionElement.Compiled) right).extract(context, extractor);
            else throw new CalculateException("Selection operator must be given a variable/function as right argument");
        } catch (ObjectExtractor.ExtractException e) {
            throw new CalculateException(e);
        }
    }
}
