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

import java.util.Date;
import java.util.regex.Matcher;
import static java.util.regex.Pattern.compile;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.jtwig.Environment;
import org.jtwig.extension.api.filters.Filter;
import org.jtwig.extension.api.filters.FilterException;
import org.jtwig.extension.api.functions.FunctionException;
import org.jtwig.extension.core.functions.DateFunction;
import org.jtwig.render.RenderContext;

public class DateModifyFilter implements Filter {
    private static final String MODIFY_PATTERN = "^([\\\\+\\\\-])([0-9]+) ([a-zA-Z]+)$";

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object left, Object...args) throws FilterException {
        if (!(left instanceof DateTime)) {
            try {
                left = new DateFunction().evaluate(env, ctx, left);
            } catch (FunctionException ex) {
                throw new FilterException(ex);
            }
        }
        
        return modify((DateTime)left, args.length > 0 ? args[0].toString() : null);
    }

    private Date modify(DateTime localDateTime, String modifyString) throws FilterException {
        if (StringUtils.isBlank(modifyString)) {
            return localDateTime.toDate();
        }

        DateTime result;

        Matcher matcher = compile(MODIFY_PATTERN).matcher(modifyString);
        matcher.find();

        int signal = 1;

        if (matcher.group(1).equals("-"))
            signal = -1;

        int val = Integer.valueOf(matcher.group(2)) * signal;
        String type = matcher.group(3).toLowerCase();

        if (type.startsWith("day"))
            result = localDateTime.plusDays(val);
        else if (type.startsWith("month"))
            result = localDateTime.plusMonths(val);
        else if (type.startsWith("year"))
            result = localDateTime.plusYears(val);
        else if (type.startsWith("second"))
            result = localDateTime.plusSeconds(val);
        else if (type.startsWith("hour"))
            result = localDateTime.plusHours(val);
        else if (type.startsWith("minute"))
            result = localDateTime.plusMinutes(val);
        else
            throw new FilterException("Unknown type " + matcher.group(3));

        return result.toDate();
    }

}