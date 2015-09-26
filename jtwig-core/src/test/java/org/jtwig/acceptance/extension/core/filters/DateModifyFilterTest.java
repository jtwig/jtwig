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

package org.jtwig.acceptance.extension.core.filters;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.jtwig.JtwigModelMap;
import static org.jtwig.JtwigTemplate.inlineTemplate;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.configuration.JtwigConfigurationBuilder;
import org.jtwig.exception.RenderException;
import org.jtwig.extension.core.CoreJtwigExtension;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class DateModifyFilterTest {
    @Test
    public void generalTests() throws Exception {
        JtwigConfiguration config = JtwigConfigurationBuilder.defaultConfiguration();
        ((CoreJtwigExtension)config.getExtensions().getExtension("core")).setTimeZone(DateTimeZone.forID("-04:00"));
        
        assertThat(inlineTemplate("{{ '2011-01-01T00:00:00'|date_modify('+1 second')|date('y-m-d H:i:s') }}").render(),
                is(equalTo("2011-01-01 00:00:01")));
        assertThat(inlineTemplate("{{ '2011-01-01T00:00:01'|date_modify('-1 second')|date('y-m-d H:i:s') }}").render(),
                is(equalTo("2011-01-01 00:00:00")));
        assertThat(inlineTemplate("{{ '2011-01-01T00:00:00'|date_modify('+1 minute')|date('y-m-d H:i:s') }}").render(),
                is(equalTo("2011-01-01 00:01:00")));
        assertThat(inlineTemplate("{{ '2011-01-01T00:00:00'|date_modify('+1 hour')|date('y-m-d H:i:s') }}").render(),
                is(equalTo("2011-01-01 01:00:00")));
        assertThat(inlineTemplate("{{ '2011-01-01T00:00:00'|date_modify('+1 day')|date('y-m-d H:i:s') }}").render(),
                is(equalTo("2011-01-02 00:00:00")));
        assertThat(inlineTemplate("{{ '2011-01-01'|date_modify('+1 day')|date('y-m-d') }}").render(),
                is(equalTo("2011-01-02")));
        assertThat(inlineTemplate("{{ '2011-01-01T00:00:00'|date_modify('+1 month')|date('y-m-d H:i:s') }}").render(),
                is(equalTo("2011-02-01 00:00:00")));
        assertThat(inlineTemplate("{{ '2011-01-01'|date_modify('+1 month')|date('y-m-d') }}").render(),
                is(equalTo("2011-02-01")));
        assertThat(inlineTemplate("{{ '2011-01-01T00:00:00'|date_modify('+1 year')|date('y-m-d H:i:s') }}").render(),
                is(equalTo("2012-01-01 00:00:00")));
        assertThat(inlineTemplate("{{ '2011-01-01'|date_modify('+1 year')|date('y-m-d') }}").render(),
                is(equalTo("2012-01-01")));
        
//        assertThat(inlineTemplate("{{ null|date_modify()|date('y-m-d') }}", config).render(), // Fails on travis???
//                is(equalTo(new DateTime().toString("YYYY-MM-dd"))));
//        assertThat(inlineTemplate("{{ 'now'|date_modify()|date('y-m-d') }}", config).render(),
//                is(equalTo(new DateTime().toString("YYYY-MM-dd"))));
    }
    
    @Test(expected = RenderException.class)
    public void failsWhenGivenNonDate() throws Exception {
        JtwigModelMap model = new JtwigModelMap()
                .add("obj", new Object());
        inlineTemplate("{{ obj|date_modify() }}").render(model);
    }

    @Test(expected = RenderException.class)
    public void addUnknown() throws Exception {
        inlineTemplate("{{ '2011-01-01T00:00:00'|date_modify('+1 unknown')|date('y-m-d H:i:s') }}").render();
    }
}