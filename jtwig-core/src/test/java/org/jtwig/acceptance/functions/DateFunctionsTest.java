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

import java.util.Date;
import org.jtwig.AbstractJtwigTest;
import org.jtwig.exception.RenderException;
import static org.jtwig.util.SyntacticSugar.given;
import org.junit.Test;

public class DateFunctionsTest extends AbstractJtwigTest {
    @Test
    public void dateFormatWithDate() throws Exception {
        given(theModel().withModelAttribute("time", new Date()));
        withResource("{{ date(time, 'yyyy') }}");
        render();
    }

    @Test
    public void dateFormat() throws Exception {
        given(theModel().withModelAttribute("time", new Date()));
        withResource("{{ date(time) }}");
        render();
    }

    @Test
    public void dateModify() throws Exception {
        given(theModel().withModelAttribute("time", new Date()));
        withResource("{{ date(time, '+1 day') }}");
        render();
    }

    @Test(expected = RenderException.class)
    public void dateModifyWithWrongFormat() throws Exception {
        given(theModel().withModelAttribute("time", new Date()));
        withResource("{{ date(time, '+1 unknown') }}");
        render();
    }
}
