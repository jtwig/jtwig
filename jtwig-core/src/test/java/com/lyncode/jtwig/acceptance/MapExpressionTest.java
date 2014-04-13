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

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MapExpressionTest extends AbstractJtwigTest {
    @Test
    public void ifWithEmptyMapShouldBeTheSameAsFalse () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if (map) %}not empty{% else %}empty{% endif %}");
        JtwigContext context = new JtwigContext();
        context.withModelAttribute("map", new HashMap());
        assertThat(template.output(context), is("empty"));
    }

    @Test
    public void ifNoKeyInMapTryMethods () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{{ map.size }}");
        JtwigContext context = new JtwigContext();
        context.withModelAttribute("map", new HashMap());
        assertThat(template.output(context), is("0"));
    }
    @Test
    public void methodsAndFieldsShouldPrevail () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{{ map.size }}");
        JtwigContext context = new JtwigContext();
        HashMap value = new HashMap();
        value.put("size", "Hello!");
        context.withModelAttribute("map", value);
        assertThat(template.output(context), is("1"));
    }
}
