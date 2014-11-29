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

package com.lyncode.jtwig.acceptance;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ListExpressionTest extends AbstractJtwigTest {
    @Test
    public void integerListByComprehension () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{{ (1..5) | join(',') }}");
        JtwigModelMap context = new JtwigModelMap();
        assertThat(template.output(context), is("1,2,3,4,5"));
    }
    @Test
    public void integerListByComprehensionReverse () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{{ (5..1) | join(',') }}");
        JtwigModelMap context = new JtwigModelMap();
        assertThat(template.output(context), is("5,4,3,2,1"));
    }

    @Test
    public void characterListByComprehension () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{{ ('a'..'c') | join }}");
        JtwigModelMap context = new JtwigModelMap();
        assertThat(template.output(context), is("abc"));
    }
    @Test
    public void characterListByComprehensionReverse () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{{ ('c'.. 'a') | join }}");
        JtwigModelMap context = new JtwigModelMap();
        assertThat(template.output(context), is("cba"));
    }
}
