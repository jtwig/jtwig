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

package com.lyncode.jtwig.acceptance;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.ClasspathJtwigResource;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.resource.StringJtwigResource;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;

import java.io.ByteArrayOutputStream;

public class AbstractJtwigTest {
    private JtwigConfiguration configuration = new JtwigConfiguration();
    private JtwigParser parser = new JtwigParser(configuration.parse());
    private JtwigContext context = new JtwigContext();
    private String output;

    @Before
    public void setUp() throws Exception {
        BasicConfigurator.configure();
    }

    protected String theResultOfRendering(JtwigTemplate template) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        template.compile(parser).render(RenderContext.create(configuration.render(), context, outputStream));
        return outputStream.toString();
    }

    protected JtwigTemplate theTemplate(String content) {
        return new JtwigTemplate(content);
    }

    protected JtwigConfiguration theConfiguration() {
        return this.configuration;
    }

    protected JtwigContext aContext() {
        return context;
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
        Renderable compiled = new JtwigTemplate(resource, configuration).compile(parser);
        compiled.render(RenderContext.create(configuration.render(), context, outputStream));
        this.output = outputStream.toString();
        return output;
    }
}
