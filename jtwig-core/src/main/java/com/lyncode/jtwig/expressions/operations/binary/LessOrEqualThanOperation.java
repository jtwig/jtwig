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
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.util.RelationalOperations;

public class LessOrEqualThanOperation extends SimpleBinaryOperation {
    @Override
    public Object apply(JtwigPosition position, Object left, Object right) throws CalculateException {
        return RelationalOperations.lte(left, right);
    }
}
