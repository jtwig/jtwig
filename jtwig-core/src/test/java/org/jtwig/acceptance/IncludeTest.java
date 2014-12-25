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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import org.jtwig.AbstractJtwigTest;
import org.jtwig.exception.CompileException;
import static org.jtwig.util.SyntacticSugar.given;
import static org.jtwig.util.SyntacticSugar.then;
import org.junit.Assert;
import org.junit.Test;

public class IncludeTest extends AbstractJtwigTest {
    @Test
    public void basicExample() throws Exception {
        withResource(classpathResource("templates/acceptance/include/main.twig"));
        then(theResult(), is(equalTo("test")));
    }
    
    @Test
    public void includeWithVars() throws Exception {
        withResource(classpathResource("templates/acceptance/include/main-vars.twig"));
        then(theResult(), is(equalTo("hello, world")));
    }
    
    @Test
    public void includeOnlyCannotAccessContext() throws Exception {
        given(theModel().add("variable", "test"));
        withResource(classpathResource("templates/acceptance/include/include-only.twig"));
        then(theResult(), is(equalTo("")));
    }
    
    @Test
    public void includeWithOnlyCannotAccessContext() throws Exception {
        given(theModel().add("variable", "pink"));
        withResource(classpathResource("templates/acceptance/include/include-with-only.twig"));
        then(theResult(), is(equalTo("pink-purple-")));
    }
    
    @Test
    public void includeMissingWithoutFlagShouldThrowException() throws Exception {
        try {
            withResource(classpathResource("templates/acceptance/include/ignore-missing-exception.twig"));
            render();
            Assert.fail("Should have received a compile exception stating that the resource could not be found.");
        } catch (CompileException e) {}
    }
    
    @Test
    public void includeMissingWithFlagShouldNotThrowException() throws Exception {
        withResource(classpathResource("templates/acceptance/include/ignore-missing-working.twig"));
        then(theResult(), is(equalTo("start--end")));
    }
    
    @Test
    public void includeUsingTernaryExpression() throws Exception {
        given(theModel().add("var", false));
        withResource(classpathResource("templates/acceptance/include/include-ternary.twig"));
        then(theResult(), is(equalTo("two")));
    }
}
