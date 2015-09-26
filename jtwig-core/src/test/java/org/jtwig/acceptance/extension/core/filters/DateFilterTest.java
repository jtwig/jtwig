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

import java.util.Collections;
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
import static org.junit.Assert.fail;
import org.junit.Test;

public class DateFilterTest {
    
    @Test
    public void generalTests() throws Exception {
        DateTimeZone zone = DateTimeZone.forID("-0400");
        JtwigConfiguration config = JtwigConfigurationBuilder.defaultConfiguration();
        ((CoreJtwigExtension)config.getExtensions().getExtension("core")).setTimeZone(zone);
        
        DateTime dt = new DateTime(2014, 4, 6, 14, 5, 8, 298, zone);
        JtwigModelMap model = new JtwigModelMap(Collections.singletonMap("d", (Object)dt));
        
        assertThat(inlineTemplate("{{ d|date('F j, Y H:i', 'America/Toronto') }}", config).render(model),
                is(equalTo("April 6, 2014 14:05")));
        assertThat(inlineTemplate("{{ d|date('F j, Y H:i', 'Europe/Paris') }}", config).render(model),
                is(equalTo("April 6, 2014 20:05")));
        assertThat(inlineTemplate("{{ d|date('d/m/Y H:i:s', 'America/Toronto') }}", config).render(model),
                is(equalTo("06/04/2014 14:05:08")));
        assertThat(inlineTemplate("{{ '2008-08-07 18:11:31'|date('Y-m-d H:i:s', '-0400') }}", config).render(),
                is(equalTo("2008-08-07 18:11:31")));
        assertThat(inlineTemplate("{{ date('30-June 2008')|date('Y-m-d') }}", config).render(),
                is(equalTo("2008-06-30")));
    }
    @Test(expected = RenderException.class)
    public void throwsExceptionOnNonDate() throws Exception {
        JtwigConfiguration config = JtwigConfigurationBuilder.defaultConfiguration();
        JtwigModelMap model = new JtwigModelMap(Collections.singletonMap("a", new Object()));
        
        inlineTemplate("{{ a|date('F j, Y H:i', 'America/Toronto') }}", config).render(model);
        fail("Should have thrown exception");
    }
    
}