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

package org.jtwig.acceptance.extension.core.tokenparsers;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.jtwig.exception.RenderException;
import org.jtwig.exception.ResourceException;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class IncludeTest {
    @Test
    public void basicExample() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .classpathTemplate("templates/acceptance/include/main.twig")
            .render(model);

        assertThat(result, is(equalTo("test")));
    }
    
    @Test
    public void includeWithVars() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .classpathTemplate("templates/acceptance/include/main-vars.twig")
            .render(model);

        assertThat(result, is(equalTo("hello, world")));
    }
    
    @Test
    public void includeOnlyCannotAccessContext() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("variable", "test");

        String result = JtwigTemplate
            .classpathTemplate("templates/acceptance/include/include-only.twig")
            .render(model);

        assertThat(result, is(equalTo("")));
    }
    
    @Test
    public void includeWithOnlyCannotAccessContext() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("variable", "pink");

        String result = JtwigTemplate
            .classpathTemplate("templates/acceptance/include/include-with-only.twig")
            .render(model);

        assertThat(result, is(equalTo("pink-purple-")));
    }
    
    @Test(expected = RenderException.class)
    public void includeMissingWithoutFlagShouldThrowException() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        JtwigTemplate
            .classpathTemplate("templates/acceptance/include/ignore-missing-exception.twig")
            .render(model);
    }
    
    @Test
    public void includeMissingWithFlagShouldNotThrowException() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .classpathTemplate("templates/acceptance/include/ignore-missing-working.twig")
            .render(model);

        assertThat(result, is(equalTo("start--end")));
    }
    
    @Test
    public void includeUsingTernaryExpression() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("var", false);

        String result = JtwigTemplate
            .classpathTemplate("templates/acceptance/include/include-ternary.twig")
            .render(model);

        assertThat(result, is(equalTo("two")));
    }
}
