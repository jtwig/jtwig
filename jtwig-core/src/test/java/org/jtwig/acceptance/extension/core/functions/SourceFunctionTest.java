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
import static org.jtwig.JtwigTemplate.inlineTemplate;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.configuration.JtwigConfigurationBuilder;
import org.jtwig.exception.RenderException;
import org.jtwig.extension.core.CoreJtwigExtension;
import org.jtwig.loader.impl.ClasspathLoader;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

public class SourceFunctionTest {
    JtwigConfiguration config;
    
    @Before
    public void before() {
        config = JtwigConfigurationBuilder.newConfiguration()
                .withLoader(new ClasspathLoader())
                .build();
        config.getExtensions().addExtension(new CoreJtwigExtension(config));
    }
    
    @Test
    public void generalTests() throws Exception {
        assertThat(inlineTemplate("{{ source('templates/unit/sample.twig') }}", config).render(),
                is(equalTo("Hi!")));
        assertThat(inlineTemplate("{{ source() }}", config).render(),
                is(equalTo("")));
    }
    
    @Test(expected = RenderException.class)
    public void testInvalidTemplate() throws Exception {
        inlineTemplate("{{ source('invalid.twig') }}", config).render();
    }
}