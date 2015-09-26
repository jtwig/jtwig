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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.jtwig.configuration.JtwigConfiguration;
import static org.jtwig.configuration.JtwigConfigurationBuilder.defaultConfiguration;
import static org.jtwig.configuration.JtwigConfigurationBuilder.newConfiguration;
import org.jtwig.extension.core.CoreJtwigExtension;
import static org.jtwig.util.matchers.ExceptionMatcher.exception;

public class Issue140Test {
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
        
        JtwigModelMap model = new JtwigModelMap();

        JtwigTemplate
            .inlineTemplate("{{ var is null }}", config(true))
            .render(model);
    }

    /**
     * Evaluating an undefined variable in an "is null" statement should return
     * true when strict mode is disabled.
     * @throws Exception 
     */
    @Test
    public void outputNonexistentVarReturnsTrue() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate("{{ var is null }}", config())
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
