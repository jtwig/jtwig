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
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ParseException;
import org.jtwig.exception.RenderException;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BinaryBooleanOperatorTest extends AbstractJtwigTest {

    @Test(expected = ParseException.class)
    public void AndBadSyntax () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if (items && true) %}Hi{% endif %}", new JtwigConfiguration());
        JtwigModelMap modelMap = new JtwigModelMap();
        template.output(modelMap);
    }

    @Test
    public void AndGoodSyntax () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if (items and true) %}Hi{% endif %}", new JtwigConfiguration());
        JtwigModelMap modelMap = new JtwigModelMap();
        ArrayList<String> value = new ArrayList<String>();
        value.add("a");
        modelMap.add("items", value);
        assertThat(template.output(modelMap), is("Hi"));
    }

    @Test(expected = ParseException.class)
    public void OrBadSyntax () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if (items || true) %}Hi{% endif %}", new JtwigConfiguration());
        JtwigModelMap modelMap = new JtwigModelMap();
        template.output(modelMap);
    }

    @Test
    public void OrGoodSyntax () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if (false or items) %}Hi{% endif %}", new JtwigConfiguration());
        JtwigModelMap modelMap = new JtwigModelMap();
        ArrayList<String> value = new ArrayList<String>();
        value.add("a");
        modelMap.add("items", value);
        assertThat(template.output(modelMap), is("Hi"));
    }

    @Test
    public void StartsWith () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if ('Hello' starts with 'H') %}Hi{% endif %}", new JtwigConfiguration());
        assertThat(template.output(new JtwigModelMap()), is("Hi"));
    }

    @Test
    public void StartsWithFail () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if ('Hello' starts with 'e') %}Hi{% endif %}", new JtwigConfiguration());
        assertThat(template.output(new JtwigModelMap()), is(""));
    }

    @Test
    public void EndsWith () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if ('Hello' ends with 'llo') %}Hi{% endif %}", new JtwigConfiguration());
        assertThat(template.output(new JtwigModelMap()), is("Hi"));
    }

    @Test
    public void EndsWithFail () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if ('Hello' ends with 'a') %}Hi{% endif %}", new JtwigConfiguration());
        assertThat(template.output(new JtwigModelMap()), is(""));
    }

    @Test
    public void Matches () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if ('Hello' matches 'H.*') %}Hi{% endif %}", new JtwigConfiguration());
        assertThat(template.output(new JtwigModelMap()), is("Hi"));
    }

    @Test
    public void MatchesFail () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if ('Hello' matches '^A.*') %}Hi{% endif %}", new JtwigConfiguration());
        assertThat(template.output(new JtwigModelMap()), is(""));
    }

    @Test
    public void MatchesFail_withNull () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if (null matches '^A.*') %}Hi{% endif %}", new JtwigConfiguration());
        assertThat(template.output(new JtwigModelMap()), is(""));
    }

    @Test
    public void Contains () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if ('a' in 'abc') %}Hi{% endif %}", new JtwigConfiguration());
        assertThat(template.output(new JtwigModelMap()), is("Hi"));
    }

    @Test
    public void ContainsFail () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if ('a' in ['b','c']) %}Hi{% endif %}", new JtwigConfiguration());
        assertThat(template.output(new JtwigModelMap()), is(""));
    }

    @Test
    public void lessOrEqualTo () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if (2 <= 2) %}Hi{% endif %}", new JtwigConfiguration());
        assertThat(template.output(new JtwigModelMap()), is("Hi"));
    }

    @Test
    public void lessThan () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if (2 < 2) %}Hi{% endif %}", new JtwigConfiguration());
        assertThat(template.output(new JtwigModelMap()), is(""));
    }

    @Test
    public void greaterThan () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if (2 > 2) %}Hi{% endif %}", new JtwigConfiguration());
        assertThat(template.output(new JtwigModelMap()), is(""));
    }

    @Test
    public void greaterOrEqualThan () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{% if (2 >= 2) %}Hi{% endif %}", new JtwigConfiguration());
        assertThat(template.output(new JtwigModelMap()), is("Hi"));
    }

    @Test
    public void modOperator () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{{ 4 % 2 }}", new JtwigConfiguration());
        assertThat(template.output(new JtwigModelMap()), is("0"));
    }

    @Test
    public void endsWithNull () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{{ null ends with 'tree' }}", new JtwigConfiguration());
        assertThat(template.output(new JtwigModelMap()), is("0"));
    }

    @Test
    public void startsWithNull () throws ParseException, CompileException, RenderException {
        JtwigTemplate template = new JtwigTemplate("{{ null starts with 'tree' }}", new JtwigConfiguration());
        assertThat(template.output(new JtwigModelMap()), is("0"));
    }
}
