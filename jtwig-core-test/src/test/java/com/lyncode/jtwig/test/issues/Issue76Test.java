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

package com.lyncode.jtwig.test.issues;

import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.test.AbstractJtwigTest;
import org.junit.Test;

import static com.lyncode.jtwig.SyntacticSugar.then;
import static com.lyncode.jtwig.SyntacticSugar.when;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.fail;

public class Issue76Test extends AbstractJtwigTest {
    @Test
    public void shouldGiveNiceExplanationForNonExistingFunctions() throws Exception {
        try {
            when(jtwigRenders(templateResource("templates/issue76/test1.twig")));
            fail();
        } catch (RenderException e) {
            then(e.getCause().getMessage(), is(equalTo("templates/issue76/test1.twig -> Line 1, column 24: Function with name 'nonExistingFunction' not found")));
        }
    }

    @Test
    public void shouldGiveNiceExplanationForNonExistingVariables() throws Exception {
        try {
            when(jtwigRenders(templateResource("templates/issue76/test2.twig")));
            fail();
        } catch (RenderException e) {
            then(e.getCause().getMessage(), is(equalTo("templates/issue76/test2.twig -> Line 1, column 23: Value of nonExistingVariable is null or undefined")));
        }
    }

    @Test
    public void shouldGiveNiceExplanationForNonExistingExpression() throws Exception {
        try {
            when(jtwigRenders(template("{{ 'abc'..'abc' }}")));
            fail();
        } catch (ParseException e) {
            then(e.getMessage(), containsString("String Source -> Line 1, column 16: Only integers and characters are allowed in comprehension lists"));
        }
    }
}
