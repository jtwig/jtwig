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

package org.jtwig.expressions.operations.binary;

import org.jtwig.exception.CalculateException;
import org.jtwig.expressions.api.BinaryOperation;
import org.jtwig.expressions.api.Expression;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;

import static org.jtwig.types.Undefined.UNDEFINED;

public abstract class SimpleBinaryOperation implements BinaryOperation {
    @Override
    public Object apply(RenderContext context, JtwigPosition position, Expression left, Expression right) throws CalculateException {
        Object calculatedLeft = left.calculate(context);
        Object calculatedRight = right.calculate(context);

        if (calculatedLeft == UNDEFINED)
            calculatedLeft = null;
        if (calculatedRight == UNDEFINED)
            calculatedRight = null;

        return apply(position, calculatedLeft, calculatedRight);
    }

    protected abstract Object apply(JtwigPosition position, Object left, Object right) throws CalculateException;
}
