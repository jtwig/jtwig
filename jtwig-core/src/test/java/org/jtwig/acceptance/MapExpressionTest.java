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

package org.jtwig.acceptance;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ParseException;
import org.jtwig.exception.RenderException;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.jtwig.util.SyntacticSugar.then;
import static org.jtwig.util.SyntacticSugar.when;

public class MapExpressionTest extends AbstractJtwigTest {
    @Test
    public void mapWithStringLiteralsAsKey () throws ParseException, CompileException, RenderException {
        when(jtwigRenders(template("{% set a = { 'test two': 'details' } %}{{ a['test two'] }}")));
        then(theRenderedTemplate(), is(equalTo("details")));
    }

    @Test
    public void ifWithEmptyMapShouldBeTheSameAsFalse () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{% if (map) %}not empty{% else %}empty{% endif %}");
        JtwigModelMap context = new JtwigModelMap();
        context.withModelAttribute("map", new HashMap());
        assertThat(template.output(context), is("empty"));
    }

    @Test
    public void ifNoKeyInMapTryMethods () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{{ map.size }}");
        JtwigModelMap context = new JtwigModelMap();
        context.withModelAttribute("map", new HashMap());
        assertThat(template.output(context), is("0"));
    }
    @Test
    public void methodsAndFieldsShouldPrevail () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = JtwigTemplate.fromString("{{ map.size }}");
        JtwigModelMap context = new JtwigModelMap();
        HashMap value = new HashMap();
        value.put("size", "Hello!");
        context.withModelAttribute("map", value);
        assertThat(template.output(context), is("1"));
    }
}
