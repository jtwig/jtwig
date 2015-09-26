/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jtwig.acceptance.extension.core.functions;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import org.jtwig.JtwigModelMap;
import static org.jtwig.JtwigTemplate.inlineTemplate;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.configuration.JtwigConfigurationBuilder;
import org.jtwig.exception.RenderException;
import org.jtwig.extension.api.functions.FunctionException;
import org.jtwig.extension.core.CoreJtwigExtension;
import org.jtwig.loader.impl.ClasspathLoader;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class IncludeFunctionTest {
    @Test
    public void generalTests() throws Exception {
        JtwigConfiguration config = JtwigConfigurationBuilder.newConfiguration()
                .withLoader(new ClasspathLoader())
                .build();
        config.getExtensions().addExtension(new CoreJtwigExtension(config));
        JtwigModelMap model = new JtwigModelMap()
                .add("template", inlineTemplate("hello, world!").asTemplate());
        
        assertThat(inlineTemplate("{{ include(template) }}").render(model),
                is(equalTo("hello, world!")));
        assertThat(inlineTemplate("{{ include('templates/acceptance/include/included/first.twig') }}", config).render(model),
                is(equalTo("one")));
        assertThat(inlineTemplate("{{ include(['invalid.twig', 'templates/acceptance/include/included/first.twig']) }}", config).render(model),
                is(equalTo("one")));
        assertThat(inlineTemplate("{{ include(['invalid1.twig', 'invalid2.twig']) }}", config).render(model),
                isEmptyString());
        assertThat(inlineTemplate("{{ include() }}").render(),
                isEmptyString());
        assertThat(inlineTemplate("{{ include(null) }}").render(),
                isEmptyString());
        assertThat(inlineTemplate("{{ include('') }}").render(),
                isEmptyString());
    }
    
    @Test(expected = RenderException.class)
    public void failsToIncludeUnparseableTemplate() throws Exception {
        JtwigConfiguration config = JtwigConfigurationBuilder.newConfiguration()
                .withLoader(new ClasspathLoader())
                .build();
        config.getExtensions().addExtension(new CoreJtwigExtension(config));
        JtwigModelMap model = new JtwigModelMap()
                .add("obj", new TestObject());
        inlineTemplate("{{ include(template_from_string('{{ obj.renderException() }}')) }}", config).render(model);
    }
    
    @Test(expected = RenderException.class)
    public void throwsExceptionWhenGivenNonTemplate() throws Exception {
        inlineTemplate("{{ include(5) }}").render();
    }
    
    public static class TestObject {
        public void renderException() throws Exception {
            throw new RenderException("No reason");
        }
    }
}