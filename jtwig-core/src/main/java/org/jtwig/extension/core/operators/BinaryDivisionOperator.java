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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.jtwig.extension.api.operator.BinaryOperator;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import static org.jtwig.util.TypeUtil.toDecimal;

public class BinaryDivisionOperator extends BinaryOperator {
    
    public BinaryDivisionOperator(String name, int precedence) {
        super(name, precedence);
    }

    @Override
    public Object render(RenderContext ctx, JtwigPosition pos, Object left, Object right) {
        BigDecimal leftDecimal = toDecimal(left);
        BigDecimal rightDecimal = toDecimal(right);
        if (rightDecimal.compareTo(BigDecimal.ZERO) == 0)
            return null;
        if (rightDecimal.compareTo(BigDecimal.ONE) == 0)
            return leftDecimal.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0 ? leftDecimal.longValue() : leftDecimal;
        
        // Perform decimal arithmetic just in case
        BigDecimal result = leftDecimal.divide(rightDecimal, new MathContext(25, RoundingMode.HALF_UP));
        if (result.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
            return result.longValue();
        }
        return result;
    }
    
}