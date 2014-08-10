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

package com.lyncode.jtwig.functions.builtin;

import com.lyncode.jtwig.functions.annotations.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.Parameter;
import com.lyncode.jtwig.functions.exceptions.FunctionException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class NumberFunctions {
    @JtwigFunction(name = "number_format")
    public String numberFormat (@Parameter Object number, @Parameter Integer fractionDigits, @Parameter String decimalSeparator, @Parameter String groupingSeparator) {
        DecimalFormat numberFormat = new DecimalFormat();
        DecimalFormatSymbols decimalFormatSymbols = numberFormat.getDecimalFormatSymbols();

        if (fractionDigits != null) {
            numberFormat.setMaximumFractionDigits(fractionDigits);
            numberFormat.setMinimumFractionDigits(fractionDigits);
        }

        if (decimalSeparator != null && !decimalSeparator.isEmpty())
            decimalFormatSymbols.setDecimalSeparator(decimalSeparator.charAt(0));
        else
            decimalFormatSymbols.setDecimalSeparator('.');


        if (groupingSeparator != null && !groupingSeparator.isEmpty())
            decimalFormatSymbols.setGroupingSeparator(groupingSeparator.charAt(0));
        else
            numberFormat.setGroupingUsed(false);

        numberFormat.setDecimalFormatSymbols(decimalFormatSymbols);

        return numberFormat.format(number);
    }

    @JtwigFunction(name = "number_format")
    public String numberFormat (@Parameter Object number, @Parameter Integer fractionDigits, @Parameter String decimalSeparator) {
        return numberFormat(number, fractionDigits, decimalSeparator, null);
    }

    @JtwigFunction(name = "number_format")
    public String numberFormat (@Parameter Object number, @Parameter Integer fractionDigits) {
        return numberFormat(number, fractionDigits, null, null);
    }

    @JtwigFunction(name = "number_format")
    public String numberFormat (@Parameter Object number) {
        return numberFormat(number, null, null, null);
    }

    @JtwigFunction(name = "range")
    public List<Integer> range (@Parameter int start, @Parameter int end, @Parameter int step) throws FunctionException {
        List<Integer> result = new ArrayList<>();


        if (step == 0)
            throw new FunctionException("Step must not be 0");

        if (start > end) {
            // negate step for reversed mode, if positive input
            if (step > 0) step = -step;

            if (step < end) throw new FunctionException("Step is too big");
        } else {
            if (step > end) throw new FunctionException("Step is too big");
        }


        for (int i = start; (step > 0) ? i <= end : i >= end; i += step) {
            result.add(i);
        }

        return result;
    }
    @JtwigFunction(name = "range")
    public List<Integer> range (@Parameter int start, @Parameter int end) throws FunctionException {
        return range(start, end, 1);
    }
}
