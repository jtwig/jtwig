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

package org.jtwig.acceptance.extension.core.functions;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jtwig.JtwigModelMap;
import static org.jtwig.JtwigTemplate.inlineTemplate;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.configuration.JtwigConfigurationBuilder;
import org.jtwig.exception.RenderException;
import org.jtwig.extension.core.CoreJtwigExtension;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class DateFunctionTest {
    
    @Test
    public void generalTests() throws Exception {
        DateTimeZone zone = DateTimeZone.forID("-04:00");
        JtwigConfiguration config = JtwigConfigurationBuilder.defaultConfiguration();
        ((CoreJtwigExtension)config.getExtensions().getExtension("core")).setTimeZone(zone);
        ((CoreJtwigExtension)config.getExtensions().getExtension("core")).setDateFormat("Y-m-d H:i:s");
        
        assertThat(inlineTemplate("{{ date('now')|date }}", config).render(),
                is(equalTo(new DateTime(zone).toString("yyyy-MM-dd HH:mm:ss"))));
        assertThat(inlineTemplate("{{ date()|date }}", config).render(),
                is(equalTo(new DateTime(zone).toString("yyyy-MM-dd HH:mm:ss"))));
        assertThat(inlineTemplate("{{ date('2008-08-07 18:11:31', '-0500')|date('Y-m-d H:i:s', '-0500') }}", config).render(),
                is(equalTo("2008-08-07 18:11:31")));
        assertThat(inlineTemplate("{{ date('30-June 2008')|date('Y-m-d') }}", config).render(),
                is(equalTo("2008-06-30")));
    }
    
    @Test(expected = RenderException.class)
    public void testFailureWhenGivenObject() throws Exception {
        JtwigModelMap model = new JtwigModelMap()
                .add("obj", new Object());
        inlineTemplate("{{ date(obj) }}").render(model);
    }
    
}