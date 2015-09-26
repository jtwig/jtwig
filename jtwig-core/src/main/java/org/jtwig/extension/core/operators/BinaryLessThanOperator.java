/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.extension.core.operators;

import org.jtwig.extension.api.operator.BinaryOperator;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import static org.jtwig.util.TypeUtil.*;

public class BinaryLessThanOperator extends BinaryOperator {

    public BinaryLessThanOperator(String name, int precedence) {
        super(name, precedence);
    }

    @Override
    public Boolean render(RenderContext ctx, JtwigPosition pos, Object left, Object right) {
        if (isDecimal(left) || isDecimal(right)) {
            return toDecimal(left).compareTo(toDecimal(right)) < 0;
        }
        if (isLong(left) || isLong(right)) {
            return toLong(left) < toLong(right);
        }
        if (left instanceof CharSequence && right instanceof CharSequence) {
            return left.toString().compareTo(right.toString()) < 0;
        }
        if (left.getClass().equals(right.getClass()) && left instanceof Comparable) {
            return ((Comparable)left).compareTo(right) < 0;
        }
        return false;
    }
    
}