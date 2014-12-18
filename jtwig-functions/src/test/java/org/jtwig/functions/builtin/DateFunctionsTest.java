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

package org.jtwig.functions.builtin;

import org.jtwig.functions.exceptions.FunctionException;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DateFunctionsTest {
    private DateFunctions underTest = new DateFunctions();

    @Test
    public void addDay() throws Exception {

        String input = "2011-01-01T00:00:00";
        String target = "2011-01-02T00:00:00";
        String modifyString = "+1 day";

        testModify(input, target, modifyString);

        input = "2011-01-01";
        target = "2011-01-02";
        modifyString = "+1 day";

        testModify(input, target, modifyString);
    }

    @Test
    public void addMonth() throws Exception {
        String input = "2011-01-01T00:00:00";
        String target = "2011-02-01T00:00:00";
        String modifyString = "+1 month";

        testModify(input, target, modifyString);

        input = "2011-01-01";
        target = "2011-02-01";
        modifyString = "+1 month";

        testModify(input, target, modifyString);
    }

    @Test
    public void addYear() throws Exception {
        String input = "2011-01-01T00:00:00";
        String target = "2012-01-01T00:00:00";
        String modifyString = "+1 year";

        testModify(input, target, modifyString);

        input = "2011-01-01T00:00:00";
        target = "2012-01-01T00:00:00";
        modifyString = "+1 year";

        testModify(input, target, modifyString);
    }

    @Test
    public void addMinute() throws Exception {
        String input = "2011-01-01T00:00:00";
        String target = "2011-01-01T00:01:00";
        String modifyString = "+1 minute";

        testModify(input, target, modifyString);
    }

    @Test
    public void addSecond() throws Exception {
        String input = "2011-01-01T00:00:00";
        String target = "2011-01-01T00:00:01";
        String modifyString = "+1 second";

        testModify(input, target, modifyString);
    }

    @Test
    public void addHour() throws Exception {
        String input = "2011-01-01T00:00:00";
        String target = "2011-01-01T01:00:00";
        String modifyString = "+1 hour";

        testModify(input, target, modifyString);
    }

    @Test(expected = FunctionException.class)
    public void addUnknown() throws Exception {
        String input = "2011-01-01T00:00:00";
        String target = "2011-01-01T01:00:00";
        String modifyString = "+1 unknown";

        testModify(input, target, modifyString);
    }

    @Test
    public void formatExecuteWithoutFormat() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2011-01-01 00:00:00");
        String result = underTest.format(date);
        assertEquals("2011-01-01T00:00:00", result);
    }

    @Test
    public void formatExecuteWithFormat() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2011-01-01 00:00:00");
        String result = underTest.format(date, "yyyy");
        assertEquals("2011", result);
    }

    @Test
    public void formatExecuteWithFormatString() throws Exception {
        String result = underTest.format("2014-12-18", "yyyy");
        assertEquals("2014", result);
    }

    @Test
    public void formatExecuteWithoutFormatString() throws Exception {
        String result = underTest.format("2011-01-01T00:00:00");
        assertEquals("2011-01-01T00:00:00", result);
    }

    private void testModify(String input, String target, String modifyString) throws FunctionException {

        LocalDateTime inputDateTime = LocalDateTime.parse(input);
        LocalDateTime targetDateTime = LocalDateTime.parse(target);

        assertEquals(targetDateTime.toDate(), underTest.modifyDate(inputDateTime.toDate(), modifyString));
        assertEquals(targetDateTime.toDate(), underTest.modifyDate(input, modifyString));

    }
}
