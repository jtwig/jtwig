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
import org.jtwig.AbstractJtwigTest;
import org.jtwig.exception.ParseException;
import org.junit.Test;

public class BinaryBooleanOperatorTest extends AbstractJtwigTest {

    @Test(expected = ParseException.class)
    public void AndBadSyntax () throws Exception {
        withResource("{% if (items && true) %}Hi{% endif %}");
        render();
    }

    @Test
    public void AndGoodSyntax () throws Exception {
        withResource("{% if (items and true) %}Hi{% endif %}");
        model.add("items", Arrays.asList("a"));
        assertThat(theResult(), is("Hi"));
    }

    @Test(expected = ParseException.class)
    public void OrBadSyntax () throws Exception {
        withResource("{% if (items || true) %}Hi{% endif %}");
        render();
    }

    @Test
    public void OrGoodSyntax () throws Exception {
        withResource("{% if (false or items) %}Hi{% endif %}");
        model.add("items", Arrays.asList("a"));
        assertThat(theResult(), is("Hi"));
    }

    @Test
    public void StartsWith () throws Exception {
        withResource("{% if ('Hello' starts with 'H') %}Hi{% endif %}");
        assertThat(theResult(), is("Hi"));
    }

    @Test
    public void StartsWithFail () throws Exception {
        withResource("{% if ('Hello' starts with 'e') %}Hi{% endif %}");
        assertThat(theResult(), is(""));
    }

    @Test
    public void EndsWith () throws Exception {
        withResource("{% if ('Hello' ends with 'llo') %}Hi{% endif %}");
        assertThat(theResult(), is("Hi"));
    }

    @Test
    public void EndsWithFail () throws Exception {
        withResource("{% if ('Hello' ends with 'a') %}Hi{% endif %}");
        assertThat(theResult(), is(""));
    }

    @Test
    public void Matches () throws Exception {
        withResource("{% if ('Hello' matches 'H.*') %}Hi{% endif %}");
        assertThat(theResult(), is("Hi"));
    }

    @Test
    public void MatchesFail () throws Exception {
        withResource("{% if ('Hello' matches '^A.*') %}Hi{% endif %}");
        assertThat(theResult(), is(""));
    }

    @Test
    public void MatchesFail_withNull () throws Exception {
        withResource("{% if (null matches '^A.*') %}Hi{% endif %}");
        assertThat(theResult(), is(""));
    }

    @Test
    public void Contains () throws Exception {
        withResource("{% if ('a' in 'abc') %}Hi{% endif %}");
        assertThat(theResult(), is("Hi"));
    }

    @Test
    public void ContainsFail () throws Exception {
        withResource("{% if ('a' in ['b','c']) %}Hi{% endif %}");
        assertThat(theResult(), is(""));
    }

    @Test
    public void lessOrEqualTo () throws Exception {
        withResource("{% if (2 <= 2) %}Hi{% endif %}");
        assertThat(theResult(), is("Hi"));
    }

    @Test
    public void lessThan () throws Exception {
        withResource("{% if (2 < 2) %}Hi{% endif %}");
        assertThat(theResult(), is(""));
    }

    @Test
    public void greaterThan () throws Exception {
        withResource("{% if (2 > 2) %}Hi{% endif %}");
        assertThat(theResult(), is(""));
    }

    @Test
    public void greaterOrEqualThan () throws Exception {
        withResource("{% if (2 >= 2) %}Hi{% endif %}");
        assertThat(theResult(), is("Hi"));
    }

    @Test
    public void modOperator () throws Exception {
        withResource("{{ 4 % 2 }}");
        assertThat(theResult(), is("0"));
    }

    @Test
    public void endsWithNull () throws Exception {
        withResource("{{ null ends with 'tree' }}");
        assertThat(theResult(), is("0"));
    }

    @Test
    public void startsWithNull () throws Exception {
        withResource("{{ null starts with 'tree' }}");
        assertThat(theResult(), is("0"));
    }
}
