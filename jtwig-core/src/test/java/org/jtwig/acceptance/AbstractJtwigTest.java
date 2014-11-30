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

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.content.api.Renderable;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ParseException;
import org.jtwig.exception.RenderException;
import org.jtwig.parser.JtwigParser;
import org.jtwig.render.RenderContext;
import org.jtwig.resource.ClasspathJtwigResource;
import org.jtwig.resource.JtwigResource;
import org.jtwig.resource.StringJtwigResource;

import java.io.ByteArrayOutputStream;

public class AbstractJtwigTest {
    private JtwigConfiguration configuration = new JtwigConfiguration();
    private JtwigParser parser = new JtwigParser(configuration.parse());
    private JtwigModelMap model = new JtwigModelMap();
    private String output;

    protected String theResultOfRendering(JtwigTemplate template) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        template.compile().render(RenderContext.create(configuration.render(), model, outputStream));
        return outputStream.toString();
    }

    protected JtwigTemplate theTemplate(String content) {
        return new JtwigTemplate(content, configuration);
    }

    protected JtwigConfiguration theConfiguration() {
        return this.configuration;
    }
    protected JtwigModelMap aModel() {
        return this.model;
    }

    protected String theRenderedTemplate() {
        return output;
    }

    protected ClasspathJtwigResource templateResource(String resource) {
        return new ClasspathJtwigResource(resource);
    }

    protected JtwigResource template(String template) {
        return new StringJtwigResource(template);
    }

    protected String jtwigRenders(JtwigResource resource) throws ParseException, CompileException, RenderException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Renderable compiled = new JtwigTemplate(resource, configuration).compile();
        compiled.render(RenderContext.create(configuration.render(), model, outputStream));
        this.output = outputStream.toString();
        return output;
    }
}
