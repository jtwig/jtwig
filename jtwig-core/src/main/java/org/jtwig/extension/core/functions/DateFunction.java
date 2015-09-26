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

package org.jtwig.extension.core.functions;

import com.crosstreelabs.phpfunctions.DateParsingUtil;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.jtwig.Environment;
import org.jtwig.extension.api.functions.Function;
import org.jtwig.extension.api.functions.FunctionException;
import org.jtwig.extension.core.CoreJtwigExtension;
import org.jtwig.render.RenderContext;
import org.jtwig.types.Undefined;

public class DateFunction implements Function {
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .append(null, new DateTimeParser[]{
                DateParsingUtil.TWELVE_HOUR_TIME.getParser(),
                DateParsingUtil.TWENTY_FOUR_HOUR_TIME.getParser(),
                DateParsingUtil.TIMEZONE.getParser(),
                DateParsingUtil.DATE_FORMATS.getParser(),
                DateParsingUtil.ISO_DATE_FORMATS.getParser(),
                DateParsingUtil.COMPOUND_FORMATS.getParser()
            })
            .toFormatter()
            .withOffsetParsed();

    @Override
    public DateTime evaluate(Environment env, RenderContext ctx, Object... args) throws FunctionException {
        Object date = args.length > 0 ? args[0] : null;
        DateTimeZone timeZone = ((CoreJtwigExtension)env.getConfiguration().getExtensions().getExtension("core")).getTimeZone();
        if (args.length > 1 && !args[1].toString().trim().isEmpty()) {
            timeZone = DateTimeZone.forID(args[1].toString());
        }
        if (date == null || date instanceof Undefined || "now".equals(date.toString())) {
            return new DateTime(timeZone);
        }
        
        if (date instanceof Date) {
            return new DateTime((Date)date, timeZone);
        } else if (date instanceof DateTime) {
            return ((DateTime)date)
                    .withZone(timeZone);
        } else if (date instanceof CharSequence) {
            return FORMATTER.parseDateTime(date.toString())
                    .withZoneRetainFields(timeZone);
        }
        throw new FunctionException("Object of class "+date.getClass().getName()+" could not be converted to date.");
    }
    
}