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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;

public class DateFunctions {
    private static final String MODIFY_PATTERN = "^([\\\\+\\\\-])([0-9]+) ([a-zA-Z]+)$";

    @JtwigFunction(name = "date_modify")
    public Date modifyDate (@Parameter Date input, @Parameter String modifyString) throws FunctionException {

        Calendar instance = Calendar.getInstance();
        instance.setTime(input);

        Matcher matcher = compile(MODIFY_PATTERN).matcher(modifyString);
        matcher.find();
        int signal = 1;

        if (matcher.group(1).equals("-"))
            signal = -1;

        int val = Integer.valueOf(matcher.group(2)) * signal;
        String type = matcher.group(3).toLowerCase();

        if (type.startsWith("day"))
            instance.add(Calendar.DAY_OF_YEAR, val);
        else if (type.startsWith("month"))
            instance.add(Calendar.MONTH, val);
        else if (type.startsWith("year"))
            instance.add(Calendar.YEAR, val);
        else if (type.startsWith("second"))
            instance.add(Calendar.SECOND, val);
        else if (type.startsWith("hour"))
            instance.add(Calendar.HOUR, val);
        else if (type.startsWith("minute"))
            instance.add(Calendar.MINUTE, val);
        else
            throw new FunctionException("Unknown type "+matcher.group(3));

        return instance.getTime();
    }

    @JtwigFunction(name = "date")
    public String format (@Parameter Date input, @Parameter String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(input);
    }

    @JtwigFunction(name = "date")
    public String format (@Parameter Date input) {
        return format(input, "yyyy-MM-dd HH:mm:ss");
    }
}
