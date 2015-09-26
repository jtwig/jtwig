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

import org.jtwig.JtwigModelMap;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.jtwig.JtwigTemplate.inlineTemplate;
import static org.hamcrest.Matchers.*;
import org.jtwig.exception.RenderException;

public class TemplateFromStringFunctionTest {
    @Test
    public void generalTests() throws Exception {
        JtwigModelMap model = new JtwigModelMap()
                .add("name", "world");
        
        assertThat(inlineTemplate("{{ include(template_from_string('Hello {{ name }}')) }}").render(model),
                is(equalTo("Hello world")));
    }
    @Test(expected = RenderException.class)
    public void throwsExceptionOnNonStringArgument() throws Exception {
        inlineTemplate("{{ include(template_from_string([])) }}").render();
    }
    @Test(expected = RenderException.class)
    public void throwsExceptionOnParsingFailure() throws Exception {
        inlineTemplate("{{ include(template_from_string('Hello {{ name')) }}").render();
    }
}