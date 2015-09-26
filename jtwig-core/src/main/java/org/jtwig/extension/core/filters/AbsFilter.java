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
import org.jtwig.Environment;
import org.jtwig.extension.api.filters.Filter;
import org.jtwig.render.RenderContext;
import org.jtwig.util.TypeUtil;

public class AbsFilter implements Filter {

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object left, Object... args) {
        if (left instanceof BigDecimal) {
            return ((BigDecimal)left).abs();
        }
        if (left instanceof Double) {
            return Math.abs((Double)left);
        }
        if (left instanceof Float) {
            return Math.abs((Float)left);
        }
        if (left instanceof Long) {
            return Math.abs((Long)left);
        }
        if (left instanceof Integer) {
            return Math.abs((Integer)left);
        }
        if (TypeUtil.isDecimal(left) || left instanceof String) {
            return TypeUtil.toDecimal(left).abs();
        }
        return TypeUtil.toLong(left);
    }
    
}