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

package org.jtwig.acceptance.issues;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.jtwig.exception.CalculateException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jtwig.configuration.JtwigConfigurationBuilder.newConfiguration;
import static org.jtwig.util.matchers.ExceptionMatcher.exception;

public class Issue112Test {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    // 1.A
    @Test
    public void outputNonexistentVarReturnsEmpty() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ nonexistent }}", newConfiguration()
                .withStrictMode(false)
                .build())
            .render(model);

        assertThat(result, is(equalTo("")));
    }

    // 1.B
    @Test
    public void outputNonexistentVarThrowsException() throws Exception {
        expectedException.expect(exception().withInnerException(exception().ofType(CalculateException.class).message(endsWith("Variable 'nonexistent' does not exist"))));

        JtwigModelMap model = new JtwigModelMap();

        JtwigTemplate
            .inlineTemplate("{{ nonexistent }}", newConfiguration()
                .withStrictMode(true)
                .build())
            .render(model);

    }

    // 2.A
    @Test
    public void selectionExampleWithStrictValidation() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ undefinedVar.length }}", newConfiguration()
                .withStrictMode(false)
                .build())
            .render(model);

        assertThat(result, is(equalTo("")));
    }

    // 2.B
    @Test
    public void nestedSelectionExampleWithStrictValidation() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ undefinedVar.length.another }}", newConfiguration()
                .withStrictMode(false)
                .build())
            .render(model);

        assertThat(result, is(equalTo("")));
    }

    // 2.C
    @Test
    public void selectionExampleWithoutStrictValidation() throws Exception {
        expectedException.expect(exception().withInnerException(exception().ofType(CalculateException.class).message(endsWith("Variable 'undefinedVar' does not exist"))));


        JtwigModelMap model = new JtwigModelMap();

        JtwigTemplate
            .inlineTemplate("{{ undefinedVar.length }}", newConfiguration()
                .withStrictMode(true)
                .build())
            .render(model);
    }

    // 3.A
    @Test
    public void operationExampleWithoutStrictValidation () throws Exception {JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ undefinedVar + 3 }}", newConfiguration()
                .withStrictMode(false)
                .build())
            .render(model);

        assertThat(result, is(equalTo("3")));
    }

    // 3.B
    @Test
    public void operationExampleWithStrictValidation () throws Exception {
        expectedException.expect(exception().withInnerException(exception().ofType(CalculateException.class).message(endsWith("Variable 'undefinedVar' does not exist"))));

        JtwigModelMap model = new JtwigModelMap();

        JtwigTemplate
            .inlineTemplate("{{ undefinedVar + 3 }}", newConfiguration()
                .withStrictMode(true)
                .build())
            .render(model);
    }

    // 3.C
    @Test
    public void operationWithNonexistentVarThrowsException() throws Exception {
        expectedException.expect(exception().withInnerException(exception().ofType(CalculateException.class).message(endsWith("Variable 'b' does not exist"))));

        JtwigModelMap model = new JtwigModelMap();

        JtwigTemplate
            .inlineTemplate("{% set a = 5 %}{{ a - b  }}", newConfiguration()
                .withStrictMode(true)
                .build())
            .render(model);
    }

    // 3.D
    @Test
    public void subtractOperationWithNullVarInStrictMode() throws Exception {JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% set a = 5 %}{% set b = null %}{{ a - b }}", newConfiguration()
                .withStrictMode(true)
                .build())
            .render(model);

        assertThat(result, is(equalTo("5")));
    }

    // 3.E
    @Test
    public void subtractOperationWithNullVarInNonStrictMode() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% set a = 5 %}{% set b = null %}{{ a - b }}", newConfiguration()
                .withStrictMode(false)
                .build())
            .render(model);

        assertThat(result, is(equalTo("5")));
    }

    // 3.F
    @Test
    public void divOperationWithNullVarInStrictMode() throws Exception {
        expectedException.expect(exception().withInnerException(exception().ofType(CalculateException.class).message(endsWith("Division by zero"))));

        JtwigModelMap model = new JtwigModelMap();

        JtwigTemplate
            .inlineTemplate("{% set a = 5 %}{% set b = null %}{{ a / b }}", newConfiguration()
                .withStrictMode(true)
                .build())
            .render(model);
    }

    // 3.G
    @Test
    public void divOperationWithNullVarInNonStrictMode() throws Exception {
        expectedException.expect(exception().withInnerException(exception().ofType(CalculateException.class).message(endsWith("Division by zero"))));

        JtwigModelMap model = new JtwigModelMap();

        JtwigTemplate
            .inlineTemplate("{% set a = 5 %}{% set b = null %}{{ a / b }}", newConfiguration()
                .withStrictMode(false)
                .build())
            .render(model);
    }

    // 4.A
    @Test
    public void outputNullVarReturnsEmpty() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% set nothing = null %}{{ nothing }}", newConfiguration()
                .withStrictMode(false)
                .build())
            .render(model);

        assertThat(result, is(equalTo("")));
    }

    // 4.B
    @Test
    public void outputNullVarThrowsException() throws Exception {JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{% set nothing = null %}{{ nothing }}", newConfiguration()
                .withStrictMode(true)
                .build())
            .render(model);

        assertThat(result, is(equalTo("")));
    }


    @Test
    public void booleanValuesShouldBePrintItsIntegerRepresentation() throws Exception {JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ true }}", newConfiguration()
                .withStrictMode(false)
                .build())
            .render(model);

        assertThat(result, is(equalTo("1")));
    }

    @Test
    public void comparisonBetweenUndefinedAndZero() throws Exception {JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ nothing == 0 }}", newConfiguration()
                .withStrictMode(false)
                .build())
            .render(model);

        assertThat(result, is(equalTo("1")));
    }

    @Test
    public void comparisonBetweenUndefinedAndNull() throws Exception {JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ nothing == null }}", newConfiguration()
                .withStrictMode(false)
                .build())
            .render(model);

        assertThat(result, is(equalTo("1")));
    }
}
