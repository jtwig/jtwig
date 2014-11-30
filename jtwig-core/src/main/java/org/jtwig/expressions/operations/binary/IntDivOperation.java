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
import org.jtwig.parser.model.JtwigPosition;

import static org.jtwig.util.MathOperations.intDiv;
import static org.jtwig.util.MathOperations.toInt;

public class IntDivOperation extends SimpleBinaryOperation {
    @Override
    public Object apply(JtwigPosition position, Object left, Object right) throws CalculateException {
        if (toInt(right) == 0)
            throw new CalculateException(position+": Division by zero");
        return intDiv(left, right);
    }
}
