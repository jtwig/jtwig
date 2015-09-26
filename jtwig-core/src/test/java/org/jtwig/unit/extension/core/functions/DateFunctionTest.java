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
package org.jtwig.unit.extension.core.functions;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jtwig.extension.core.CoreJtwigExtension;
import org.jtwig.extension.core.functions.DateFunction;
import org.jtwig.unit.AbstractJtwigTest;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

public class DateFunctionTest extends AbstractJtwigTest {
    DateFunction underTest = new DateFunction();
    DateTime testDate = new DateTime(2008, 8, 7, 18, 11, 31, 0, DateTimeZone.forOffsetHours(-6));
    
    @Before
    @Override
    public void before() throws Exception {
        super.before();
        DateTimeZone zone = DateTimeZone.forOffsetHours(-4);
        ((CoreJtwigExtension)env.getConfiguration().getExtensions().getExtension("core")).setTimeZone(zone);
        ((CoreJtwigExtension)env.getConfiguration().getExtensions().getExtension("core")).setDateFormat("Y-m-d h:i:s");
    }
    
    @Test
    public void testStringParseWithDefaultTimezone() throws Exception {
        DateTime result = underTest.evaluate(env, renderContext, "2008-08-07 18:11:31");
        assertThat(result.toString("yyyy-MM-dd HH:mm:ss ZZ"),
                is(equalTo("2008-08-07 18:11:31 -04:00")));
    }
    
    @Test
    public void testStringParseWithExplicitTimezone() throws Exception {
        DateTime result = underTest.evaluate(env, renderContext, "2008-08-07 18:11:31", "-0600");
        assertThat(result.toString("yyyy-MM-dd HH:mm:ss ZZ"),
                is(equalTo("2008-08-07 18:11:31 -06:00")));
    }
    
    @Test
    public void testGivenDateWithDefaultTimezone() throws Exception {
        DateTime result = underTest.evaluate(env, renderContext, testDate);
        assertThat(result.toString("yyyy-MM-dd HH:mm:ss ZZ"),
                is(equalTo("2008-08-07 20:11:31 -04:00")));
    }
    
    @Test
    public void testGivenDateWithExplicitTimezone() throws Exception {
        DateTime result = underTest.evaluate(env, renderContext, testDate, "-0400");
        assertThat(result.toString("yyyy-MM-dd HH:mm:ss ZZ"),
                is(equalTo("2008-08-07 20:11:31 -04:00")));
    }
}