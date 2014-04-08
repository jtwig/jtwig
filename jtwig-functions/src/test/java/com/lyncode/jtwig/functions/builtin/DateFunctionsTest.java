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

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DateFunctionsTest {
    private DateFunctions underTest = new DateFunctions();

    @Test
    public void testExecute() throws Exception {
        Date date = new Date();
        assertNotEquals(date, underTest.modifyDate(date, "+1 day"));
    }

    @Test
    public void formatExecuteWithoutFormat() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2011-01-01 00:00:00");
        String result = underTest.format(date);
        assertEquals("2011-01-01 00:00:00", result);
    }
    @Test
    public void formatExecuteWithFormat() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2011-01-01 00:00:00");
        String result = underTest.format(date, "yyyy");
        assertEquals("2011", result);
    }
}
