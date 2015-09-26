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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import org.jtwig.Environment;
import org.jtwig.extension.api.filters.Filter;
import org.jtwig.render.RenderContext;
import org.jtwig.types.Undefined;
import org.jtwig.util.TypeUtil;

public class NumberFormatFilter implements Filter {

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object left, Object... args) {
        if (left == null || left == Undefined.UNDEFINED) {
            left = 0;
        }
        if (TypeUtil.isLong(left)) {
            left = TypeUtil.toLong(left);
        }
        if (TypeUtil.isDecimal(left)) {
            left = TypeUtil.toDecimal(left);
        }

        DecimalFormat numberFormat = new DecimalFormat();
        DecimalFormatSymbols decimalFormatSymbols = numberFormat.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        numberFormat.setGroupingUsed(false);

        if (args.length > 0 && args[0] != null) {
            numberFormat.setMaximumFractionDigits(TypeUtil.toLong(args[0]).intValue());
            numberFormat.setMinimumFractionDigits(TypeUtil.toLong(args[0]).intValue());
        }

        if (args.length > 1 && args[1] != null && !args[1].toString().isEmpty()) {
            decimalFormatSymbols.setDecimalSeparator(args[1].toString().charAt(0));
        }

        if (args.length > 2 && args[2] != null && !args[2].toString().isEmpty()) {
            decimalFormatSymbols.setGroupingSeparator(args[2].toString().charAt(0));
            numberFormat.setGroupingUsed(true);
        }

        numberFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        return numberFormat.format(left);
    }
    
}