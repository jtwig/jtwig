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

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.jtwig.exception.ParseException;
import org.junit.Test;

public class BinaryBooleanOperatorTest {

    @Test(expected = ParseException.class)
    public void AndBadSyntax () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        JtwigTemplate
            .inlineTemplate("{% if (items && true) %}Hi{% endif %}")
            .render(model);
    }

    @Test
    public void AndGoodSyntax () throws Exception {
        JtwigModelMap model = new JtwigModelMap()
            .withModelAttribute("items", Arrays.asList("a"));

        String result = JtwigTemplate
            .inlineTemplate("{% if (items and true) %}Hi{% endif %}")
            .render(model);

        assertThat(result, is("Hi"));
    }

    @Test(expected = ParseException.class)
    public void OrBadSyntax () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        JtwigTemplate
            .inlineTemplate("{% if (items || true) %}Hi{% endif %}")
            .render(model);
    }

    @Test
    public void OrGoodSyntax () throws Exception {
        JtwigModelMap model = new JtwigModelMap().withModelAttribute("items", Arrays.asList("a"));

        String result = JtwigTemplate
            .inlineTemplate("{% if (false or items) %}Hi{% endif %}")
            .render(model);

        assertThat(result, is("Hi"));
    }

    @Test
    public void StartsWith () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if ('Hello' starts with 'H') %}Hi{% endif %}")
            .render(model);

        assertThat(result, is("Hi"));
    }

    @Test
    public void StartsWithFail () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if ('Hello' starts with 'e') %}Hi{% endif %}")
            .render(model);

        assertThat(result, is(""));
    }

    @Test
    public void EndsWith () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if ('Hello' ends with 'llo') %}Hi{% endif %}")
            .render(model);

        assertThat(result, is("Hi"));
    }

    @Test
    public void EndsWithFail () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if ('Hello' ends with 'a') %}Hi{% endif %}")
            .render(model);

        assertThat(result, is(""));
    }

    @Test
    public void Matches () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if ('Hello' matches 'H.*') %}Hi{% endif %}")
            .render(model);

        assertThat(result, is("Hi"));
    }

    @Test
    public void MatchesFail () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if ('Hello' matches '^A.*') %}Hi{% endif %}")
            .render(model);

        assertThat(result, is(""));
    }

    @Test
    public void MatchesFail_withNull () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if (null matches '^A.*') %}Hi{% endif %}")
            .render(model);

        assertThat(result, is(""));
    }

    @Test
    public void Contains () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if ('a' in 'abc') %}Hi{% endif %}")
            .render(model);

        assertThat(result, is("Hi"));
    }

    @Test
    public void ContainsFail () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% if ('a' in ['b','c']) %}Hi{% endif %}")
            .render(model);

        assertThat(result, is(""));
    }

    @Test
    public void lessOrEqualTo () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        assertThat(JtwigTemplate
            .inlineTemplate("{% if (\"test1\" <= \"test2\") %}Hi{% endif %}")
            .render(model), is("Hi"));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (2 <= 2) %}Hi{% endif %}")
                       .render(model), is("Hi"));
        assertThat(JtwigTemplate
            .inlineTemplate("{% if (\"test2\" <= \"test1\") %}Hi{% endif %}")
            .render(model), is(""));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (\"test1\" <= \"test1\") %}Hi{% endif %}")
                       .render(model), is("Hi"));
        assertThat(JtwigTemplate
            .inlineTemplate("{% if (date('2014-12-12') <= date('2015-01-01')) %}Hi{% endif %}")
            .render(model), is("Hi"));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (date('2015-01-01') <= date('2014-12-12')) %}Hi{% endif %}")
                       .render(model), is(""));
        assertThat(JtwigTemplate
            .inlineTemplate("{% if (date('2015-01-01') <= date('2015-01-01')) %}Hi{% endif %}")
            .render(model), is("Hi"));
    }

    @Test
    public void lessThan () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (2 < 2) %}Hi{% endif %}")
                       .render(model), is(""));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (\"test1\" < \"test2\") %}Hi{% endif %}")
                       .render(model), is("Hi"));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (\"test2\" < \"test1\") %}Hi{% endif %}")
                       .render(model), is(""));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (\"test1\" < \"test1\") %}Hi{% endif %}")
                       .render(model), is(""));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (date('2014-12-12') < date('2015-01-01')) %}Hi{% endif %}")
                       .render(model), is("Hi"));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (date('2015-01-01') < date('2014-12-12')) %}Hi{% endif %}")
                       .render(model), is(""));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (date('2015-01-01') < date('2015-01-01')) %}Hi{% endif %}")
                       .render(model), is(""));
    }

    @Test
    public void greaterThan () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (2 > 2) %}Hi{% endif %}")
                       .render(model), is(""));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (\"test1\" > \"test2\") %}Hi{% endif %}")
                       .render(model), is(""));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (\"test2\" > \"test1\") %}Hi{% endif %}")
                       .render(model), is("Hi"));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (\"test1\" > \"test1\") %}Hi{% endif %}")
                       .render(model), is(""));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (date('2014-12-12') > date('2015-01-01')) %}Hi{% endif %}")
                       .render(model), is(""));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (date('2015-01-01') > date('2014-12-12')) %}Hi{% endif %}")
                       .render(model), is("Hi"));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (date('2015-01-01') > date('2015-01-01')) %}Hi{% endif %}")
                       .render(model), is(""));
    }

    @Test
    public void greaterOrEqualThan () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (2 >= 2) %}Hi{% endif %}")
                       .render(model), is("Hi"));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (\"test1\" >= \"test2\") %}Hi{% endif %}")
                       .render(model), is(""));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (\"test2\" >= \"test1\") %}Hi{% endif %}")
                       .render(model), is("Hi"));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (\"test1\" >= \"test1\") %}Hi{% endif %}")
                       .render(model), is("Hi"));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (date('2014-12-12') >= date('2015-01-01')) %}Hi{% endif %}")
                       .render(model), is(""));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (date('2015-01-01') >= date('2014-12-12')) %}Hi{% endif %}")
                       .render(model), is("Hi"));
        assertThat(JtwigTemplate
                       .inlineTemplate("{% if (date('2015-01-01') >= date('2015-01-01')) %}Hi{% endif %}")
                       .render(model), is("Hi"));
    }

    @Test
    public void modOperator () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ 4 % 2 }}")
            .render(model);

        assertThat(result, is("0"));
    }

    @Test
    public void endsWithNull () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ null ends with 'tree' }}")
            .render(model);

        assertThat(result, is("0"));
    }

    @Test
    public void startsWithNull () throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ null starts with 'tree' }}")
            .render(model);

        assertThat(result, is("0"));
    }
}
