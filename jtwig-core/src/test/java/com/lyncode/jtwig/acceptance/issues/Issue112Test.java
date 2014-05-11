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

package com.lyncode.jtwig.acceptance.issues;

import com.lyncode.jtwig.acceptance.AbstractJtwigTest;
import com.lyncode.jtwig.exception.CalculateException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.lyncode.jtwig.util.SyntacticSugar.*;
import static com.lyncode.jtwig.util.matchers.ExceptionMatcher.exception;
import static org.hamcrest.CoreMatchers.*;

public class Issue112Test extends AbstractJtwigTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    // 1.A
    @Test
    public void outputNonexistentVarReturnsEmpty() throws Exception {
        given(theConfiguration().render().strictMode(false));
        when(jtwigRenders(template("{{ nonexistent }}")));
        then(theRenderedTemplate(), is(equalTo("")));
    }

    // 1.B
    @Test
    public void outputNonexistentVarThrowsException() throws Exception {
        expectedException.expect(exception().withInnerException(exception().ofType(CalculateException.class).message(endsWith("Variable 'nonexistent' does not exist"))));

        given(theConfiguration().render().strictMode(true));
        when(jtwigRenders(template("{{ nonexistent }}")));
    }

    // 2.A
    @Test
    public void selectionExampleWithStrictValidation() throws Exception {
        given(theConfiguration().render().strictMode(false));
        when(jtwigRenders(template("{{ undefinedVar.length }}")));
        then(theRenderedTemplate(), is(equalTo("")));
    }

    // 2.B
    @Test
    public void nestedSelectionExampleWithStrictValidation() throws Exception {
        given(theConfiguration().render().strictMode(false));
        when(jtwigRenders(template("{{ undefinedVar.length.another }}")));
        then(theRenderedTemplate(), is(equalTo("")));
    }

    // 2.C
    @Test
    public void selectionExampleWithoutStrictValidation() throws Exception {
        expectedException.expect(exception().withInnerException(exception().ofType(CalculateException.class).message(endsWith("Variable 'undefinedVar' does not exist"))));

        given(theConfiguration().render().strictMode(true));
        jtwigRenders(template("{{ undefinedVar.length }}"));
    }

    // 3.A
    @Test
    public void operationExampleWithoutStrictValidation () throws Exception {
        given(theConfiguration().render().strictMode(false));
        when(jtwigRenders(template("{{ undefinedVar + 3 }}")));
        then(theRenderedTemplate(), is(equalTo("3")));
    }

    // 3.B
    @Test
    public void operationExampleWithStrictValidation () throws Exception {
        expectedException.expect(exception().withInnerException(exception().ofType(CalculateException.class).message(endsWith("Variable 'undefinedVar' does not exist"))));

        given(theConfiguration().render().strictMode(true));
        when(jtwigRenders(template("{{ undefinedVar + 3 }}")));
    }

    // 3.C
    @Test
    public void operationWithNonexistentVarThrowsException() throws Exception {
        expectedException.expect(exception().withInnerException(exception().ofType(CalculateException.class).message(endsWith("Variable 'b' does not exist"))));

        given(theConfiguration().render().strictMode(true));
        when(jtwigRenders(template("{% set a = 5 %}{{ a - b  }}")));
    }

    // 3.D
    @Test
    public void subtractOperationWithNullVarInStrictMode() throws Exception {
        given(theConfiguration().render().strictMode(true));
        when(jtwigRenders(template("{% set a = 5 %}{% set b = null %}{{ a - b }}")));
        then(theRenderedTemplate(), is(equalTo("5")));
    }

    // 3.E
    @Test
    public void subtractOperationWithNullVarInNonStrictMode() throws Exception {
        given(theConfiguration().render().strictMode(false));
        when(jtwigRenders(template("{% set a = 5 %}{% set b = null %}{{ a - b }}")));
        then(theRenderedTemplate(), is(equalTo("5")));
    }

    // 3.F
    @Test
    public void divOperationWithNullVarInStrictMode() throws Exception {
        expectedException.expect(exception().withInnerException(exception().ofType(CalculateException.class).message(endsWith("Division by zero"))));

        given(theConfiguration().render().strictMode(true));
        when(jtwigRenders(template("{% set a = 5 %}{% set b = null %}{{ a / b }}")));
    }

    // 3.G
    @Test
    public void divOperationWithNullVarInNonStrictMode() throws Exception {
        expectedException.expect(exception().withInnerException(exception().ofType(CalculateException.class).message(endsWith("Division by zero"))));

        given(theConfiguration().render().strictMode(false));
        when(jtwigRenders(template("{% set a = 5 %}{% set b = null %}{{ a / b }}")));
    }

    // 4.A
    @Test
    public void outputNullVarReturnsEmpty() throws Exception {
        given(theConfiguration().render().strictMode(false));
        when(jtwigRenders(template("{% set nothing = null %}{{ nothing }}")));
        then(theRenderedTemplate(), is(equalTo("")));
    }

    // 4.B
    @Test
    public void outputNullVarThrowsException() throws Exception {
        given(theConfiguration().render().strictMode(true));
        when(jtwigRenders(template("{% set nothing = null %}{{ nothing }}")));
        then(theRenderedTemplate(), is(equalTo("")));
    }
}