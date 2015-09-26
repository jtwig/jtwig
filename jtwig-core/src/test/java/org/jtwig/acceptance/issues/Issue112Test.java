/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jtwig.acceptance.issues;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.jtwig.exception.CalculateException;
import static org.junit.Assert.assertEquals;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.jtwig.configuration.JtwigConfiguration;
import static org.jtwig.configuration.JtwigConfigurationBuilder.newConfiguration;
import org.jtwig.extension.core.CoreJtwigExtension;
import static org.jtwig.util.matchers.ExceptionMatcher.exception;

public class Issue112Test {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    // 1.A
    @Test
    public void outputNonexistentVarReturnsEmpty() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ nonexistent }}", config())
            .render(model);

        assertThat(result, is(equalTo("")));
    }

    // 1.B
    @Test
    public void outputNonexistentVarThrowsException() throws Exception {
        expectedException.expect(exception().withInnerException(exception().ofType(CalculateException.class).message(endsWith("Variable 'nonexistent' does not exist"))));

        JtwigModelMap model = new JtwigModelMap();

        JtwigTemplate
            .inlineTemplate("{{ nonexistent }}", config(true))
            .render(model);

    }

    // 2.A
    @Test
    public void selectionExampleWithoutStrictValidation() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ undefinedVar.length }}", config())
            .render(model);

        assertThat(result, is(equalTo("")));
    }

    // 2.B
    @Test
    public void nestedSelectionExampleWithoutStrictValidation() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ undefinedVar.length.another }}", config())
            .render(model);

        assertThat(result, is(equalTo("")));
    }

    // 2.C
    @Test
    public void selectionExampleWithStrictValidation() throws Exception {
        expectedException.expect(exception().withInnerException(exception().ofType(CalculateException.class).message(endsWith("Impossible to access attribute/method 'length' on null"))));
        
        JtwigModelMap model = new JtwigModelMap();

        assertEquals("", JtwigTemplate
            .inlineTemplate("{{ undefinedVar.length }}", config(true))
            .render(model));
    }

    // 3.A
    @Test
    public void operationExampleWithoutStrictValidation () throws Exception {JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ undefinedVar + 3 }}", config())
            .render(model);

        assertThat(result, is(equalTo("3")));
    }

    // 3.B
    @Test
    public void operationExampleWithStrictValidation () throws Exception {
        expectedException.expect(exception().withInnerException(exception().ofType(CalculateException.class).message(endsWith("Variable 'undefinedVar' does not exist"))));

        JtwigModelMap model = new JtwigModelMap();

        JtwigTemplate
            .inlineTemplate("{{ undefinedVar + 3 }}", config(true))
            .render(model);
    }

    // 3.C
    @Test
    public void operationWithNonexistentVarThrowsException() throws Exception {
        expectedException.expect(exception().withInnerException(exception().ofType(CalculateException.class).message(endsWith("Variable 'b' does not exist"))));

        JtwigModelMap model = new JtwigModelMap();

        JtwigTemplate
            .inlineTemplate("{% set a = 5 %}{{ a - b  }}", config(true))
            .render(model);
    }

    // 3.D
    @Test
    public void subtractOperationWithNullVarInStrictMode() throws Exception {JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% set a = 5 %}{% set b = null %}{{ a - b }}", config(true))
            .render(model);

        assertThat(result, is(equalTo("5")));
    }

    // 3.E
    @Test
    public void subtractOperationWithNullVarInNonStrictMode() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% set a = 5 %}{% set b = null %}{{ a - b }}", config())
            .render(model);

        assertThat(result, is(equalTo("5")));
    }

    // 3.F
    @Test
    public void divOperationWithNullVarInStrictMode() throws Exception {
        // Even with strict variables on, Twig does not crap out on division by
        // zero. It raises a notice in the logs, and returns null. There is no
        // difference in behaviour between strict and non-strict mode
        JtwigModelMap model = new JtwigModelMap();

        assertEquals("", JtwigTemplate
            .inlineTemplate("{% set a = 5 %}{% set b = null %}{{ a / b }}", config(true))
            .render(model));
    }

    // 3.G
    @Test
    public void divOperationWithNullVarInNonStrictMode() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        assertEquals("", JtwigTemplate
            .inlineTemplate("{% set a = 5 %}{% set b = null %}{{ a / b }}", config())
            .render(model));
    }

    // 4.A
    @Test
    public void outputNullVarReturnsEmpty() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% set nothing = null %}{{ nothing }}", config())
            .render(model);

        assertThat(result, is(equalTo("")));
    }

    // 4.B
    @Test
    public void outputNullVarThrowsException() throws Exception {JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% set nothing = null %}{{ nothing }}", config(true))
            .render(model);

        assertThat(result, is(equalTo("")));
    }

    @Test
    public void booleanValuesShouldBePrintItsIntegerRepresentation() throws Exception {
        assertEquals("1", JtwigTemplate.inlineTemplate("{{ true }}").render());
        assertEquals("0", JtwigTemplate.inlineTemplate("{{ false }}").render());
    }

    @Test
    public void comparisonBetweenUndefinedAndZero() throws Exception {JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ nothing == 0 }}", config())
            .render(model);

        assertThat(result, is(equalTo("1")));
    }

    @Test
    public void comparisonBetweenUndefinedAndNull() throws Exception {JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ nothing == null }}", config())
            .render(model);

        assertThat(result, is(equalTo("1")));
    }
    
    protected JtwigConfiguration config() {
        return config(false);
    }
    protected JtwigConfiguration config(boolean strict) {
        JtwigConfiguration config = newConfiguration()
                .withStrictMode(strict)
                .build();
        config.getExtensions().addExtension(new CoreJtwigExtension(config));
        return config;
    }
}
