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
import org.jtwig.exception.ParseException;
import org.jtwig.functions.annotations.JtwigFunction;
import org.jtwig.functions.annotations.Parameter;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import org.jtwig.configuration.JtwigConfiguration;
import static org.jtwig.configuration.JtwigConfigurationBuilder.newConfiguration;
import org.jtwig.extension.core.CoreJtwigExtension;

public class ImportTest {
    @Test
    public void basicExample() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .classpathTemplate("templates/acceptance/import/import.twig")
            .render(model);
        
        assertThat(result, is(equalTo("\ntext (test)\n\npassword (pass)\n\npassword (password)\n")));
    }
    
    @Test(expected = ParseException.class)
    public void ensureInvalidImportStatementThrowsException() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        JtwigTemplate
            .classpathTemplate("templates/acceptance/import/multiple-import-as.twig")
            .render(model);
    }
    
    @Test
    public void ensureImportSelfWorks() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .classpathTemplate("templates/acceptance/import/import-self.twig")
            .render(model);

        assertThat(result, is(equalTo("\n\njtwig\n")));
    }
    
    @Test
    public void ensureNestedSelfImportWorks() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .classpathTemplate("templates/acceptance/import/nested-import-self-test.twig")
            .render(model);

        assertThat(result, is(equalTo("\n<input type=\"password\" name=\"jtwig\">\n\n")));
    }
    
    @Test
    public void ensureMacrosCanUseCustomFunctions() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        JtwigConfiguration config = newConfiguration()
                .include(new TypeFunction())
                .build();
        config.getExtensions().addExtension(new CoreJtwigExtension(config));
        
        String result = JtwigTemplate
            .classpathTemplate("templates/acceptance/import/macro-can-use-functions.twig", config)
            .render(model);

        assertThat(result, is(equalTo("\n\njava.lang.String\n")));
    }
    
    @Test(expected = ParseException.class)
    public void ensureInvalidFromStatementThrowsException() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        JtwigTemplate
            .classpathTemplate("templates/acceptance/import/import-string-name.twig")
            .render(model);
    }
    
    public static class TypeFunction {
        @JtwigFunction(name = "type")
        public String type(@Parameter Object obj) {
            return obj.getClass().getCanonicalName();
        }
    }
}
