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

package com.lyncode.jtwig.test;

import com.lyncode.jtwig.JtwigContext;
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
        JtwigTemplate template = new JtwigTemplate("{{ 1..5 | join(',') }}");
        JtwigContext context = new JtwigContext();
        assertThat(template.output(context), is("1,2,3,4,5"));
    }

    @Test
    public void characterListByComprehension () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{{ 'a'..'c' | join }}");
        JtwigContext context = new JtwigContext();
        assertThat(template.output(context), is("abc"));
    }
}
