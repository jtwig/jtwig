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

package com.lyncode.jtwig.functions.internal.math;

import com.lyncode.jtwig.functions.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.JtwigFunctionDeclaration;
import com.lyncode.jtwig.functions.exceptions.FunctionException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import static com.lyncode.jtwig.functions.util.Requirements.between;
import static com.lyncode.jtwig.functions.util.Requirements.requires;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

@JtwigFunctionDeclaration(name = "number_format")
public class JNumberFormat implements JtwigFunction {
    @Override
    public Object execute(Object... arguments) throws FunctionException {
        requires(arguments)
                .withNumberOfArguments(between(1, 4))
                .withArgument(1, instanceOf(Integer.class));

        DecimalFormat numberFormat = new DecimalFormat();
        DecimalFormatSymbols decimalFormatSymbols = numberFormat.getDecimalFormatSymbols();

        if (arguments.length > 1) {
            numberFormat.setMaximumFractionDigits((Integer) arguments[1]);
            numberFormat.setMinimumFractionDigits((Integer) arguments[1]);
        }

        if (arguments.length > 2) {
            String separator = arguments[2].toString();
            if (!separator.isEmpty())
                decimalFormatSymbols.setDecimalSeparator(separator.charAt(0));
            else
                decimalFormatSymbols.setDecimalSeparator('\0');
        }

        if (arguments.length > 3) {
            String separator = arguments[3].toString();
            if (!separator.isEmpty())
                decimalFormatSymbols.setGroupingSeparator(separator.charAt(0));
            else
                decimalFormatSymbols.setGroupingSeparator('\0');
        }

        numberFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        return numberFormat.format(arguments[0]);
    }
}
