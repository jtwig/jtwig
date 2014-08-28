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

package com.lyncode.jtwig.acceptance.functions;

import com.lyncode.jtwig.acceptance.AbstractJtwigTest;
import com.lyncode.jtwig.exception.RenderException;
import org.junit.Test;

import java.util.Date;

import static com.lyncode.jtwig.util.SyntacticSugar.given;
import static com.lyncode.jtwig.util.SyntacticSugar.when;

public class DateFunctionsTest extends AbstractJtwigTest {
    @Test
    public void dateFormatWithDate() throws Exception {
        given(aModel().withModelAttribute("time", new Date()));
        when(jtwigRenders(template("{{ date_format(time, 'yyyy') }}")));
    }

    @Test
    public void dateFormat() throws Exception {
        given(aModel().withModelAttribute("time", new Date()));
        when(jtwigRenders(template("{{ date_format(time) }}")));
    }

    @Test
    public void dateModify() throws Exception {
        given(aModel().withModelAttribute("time", new Date()));
        when(jtwigRenders(template("{{ date_format(time, '+1 day') }}")));
    }

    @Test(expected = RenderException.class)
    public void dateModifyWithWrongFormat() throws Exception {
        given(aModel().withModelAttribute("time", new Date()));
        when(jtwigRenders(template("{{ date_modify(time, '+1 unknown') }}")));
    }
}
