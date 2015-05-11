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

package org.jtwig.acceptance.functions;

import org.joda.time.LocalDate;
import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.jtwig.exception.RenderException;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class DateFunctionsTest {
    @Test
    public void dateFormatWithDate() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("time", LocalDate.parse("2014-01-01").toDate());

        String result = JtwigTemplate
            .inlineTemplate("{{ date(time, 'yyyy') }}")
            .render(model);

        assertThat(result, is(equalTo("2014")));
    }

    @Test
    public void dateFormat() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("time", LocalDate.parse("2014-01-01").toDate());

        String result = JtwigTemplate
            .inlineTemplate("{{ date(time) }}")
            .render(model);

        assertThat(result, is(equalTo("2014-01-01T00:00:00")));
    }

    @Test
    public void dateModify() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("time", LocalDate.parse("2014-01-01").toDate());

        String result = JtwigTemplate
            .inlineTemplate("{{ date_modify(time, '+1 day') | date('yyyy-MM-dd') }}")
            .render(model);

        assertThat(result, is(equalTo("2014-01-02")));
    }

    @Test(expected = RenderException.class)
    public void dateModifyWithWrongFormat() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("time", LocalDate.parse("2014-01-01").toDate());

        JtwigTemplate
            .inlineTemplate("{{ date_modify(time, '+1 unknown') }}")
            .render(model);
    }
}
