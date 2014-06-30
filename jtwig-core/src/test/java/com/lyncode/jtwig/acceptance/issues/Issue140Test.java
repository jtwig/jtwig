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

public class Issue140Test extends AbstractJtwigTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /**
     * Evaluating an undefined variable in an "is null" statement should throw
     * a calculate exception in strict mode.
     * @throws Exception 
     */
    @Test
    public void undefinedVarThrowsExceptionOnEvaluation() throws Exception {
        expectedException.expect(exception().withInnerException(exception().ofType(CalculateException.class)));
        given(theConfiguration().render().strictMode(true));
        when(jtwigRenders(template("{{ var is null }}")));
    }

    /**
     * Evaluating an undefined variable in an "is null" statement should return
     * true when strict mode is disabled.
     * @throws Exception 
     */
    @Test
    public void outputNonexistentVarThrowsException() throws Exception {
        given(theConfiguration().render().strictMode(false));
        String str = jtwigRenders(template("{{ var is null }}"));
        when(str);
        then(theRenderedTemplate(), is(equalTo("1")));
    }
}