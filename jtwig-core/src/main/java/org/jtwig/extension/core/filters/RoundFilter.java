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

package org.jtwig.extension.core.filters;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import org.jtwig.Environment;
import org.jtwig.extension.api.filters.Filter;
import org.jtwig.functions.annotations.JtwigFunction;
import org.jtwig.functions.annotations.Parameter;
import org.jtwig.render.RenderContext;
import org.jtwig.util.TypeUtil;

public class RoundFilter implements Filter {

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object left, Object... args) {
        int precision = 0;
        if (args.length > 0) {
            precision = TypeUtil.toLong(args[0]).intValue();
        }
        
        RoundStrategy strategy = RoundStrategy.COMMON;
        if (args.length > 1) {
            strategy = RoundStrategy.valueOf(args[1].toString().toUpperCase());
        }
        
        if (TypeUtil.isDecimal(left)) {
            return TypeUtil.toDecimal(left).setScale(precision, strategy.mode);
        }
        if (TypeUtil.isLong(left)) {
            return TypeUtil.toLong(left);
        }
        if (left instanceof CharSequence) {
            return 0; // No clue why Twig does this, as string normally == '1'
        }
        return null;
    }

    public static enum RoundStrategy {
        COMMON(RoundingMode.HALF_UP),
        CEIL(RoundingMode.UP),
        FLOOR(RoundingMode.DOWN);
        
        public final RoundingMode mode;
        
        RoundStrategy(RoundingMode mode) {
            this.mode = mode;
        }
    }
    
}