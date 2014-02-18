/**
 * Copyright 2012 Lyncode
 *
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

package com.lyncode.jtwig.functions.internal.date;

import com.lyncode.jtwig.functions.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.JtwigFunctionDeclaration;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;

import static com.lyncode.jtwig.functions.util.Requirements.requires;
import static java.util.regex.Pattern.compile;
import static org.hamcrest.CoreMatchers.*;

@JtwigFunctionDeclaration(name  = "date_modify")
public class DateModify implements JtwigFunction {

    private static final String PATTERN = "^([\\\\+\\\\-])([0-9]+) ([a-zA-Z]+)$";

    @Override
    public Object execute(Object... arguments) throws FunctionException {
        requires(arguments)
                .withNumberOfArguments(equalTo(2))
                .withArgument(0, instanceOf(Date.class))
                .withArgument(1, notNullValue())
                .withArgument(1, hasPattern(PATTERN));


        Calendar instance = Calendar.getInstance();
        instance.setTime((Date) arguments[0]);

        String modifyString = arguments[1].toString().trim();

        Matcher matcher = compile(PATTERN).matcher(modifyString);
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

    private BaseMatcher<Object> hasPattern(final String pattern) {
        return new BaseMatcher<Object>() {
            @Override
            public boolean matches(Object item) {
                return item.toString().matches(pattern);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("matches pattern ").appendValue(pattern);
            }
        };
    }
}
