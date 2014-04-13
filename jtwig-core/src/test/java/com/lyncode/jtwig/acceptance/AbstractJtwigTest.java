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
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.parser.JtwigParserBuilder;
import com.lyncode.jtwig.tree.helper.RenderStream;
import com.lyncode.jtwig.unit.resource.ClasspathJtwigResource;
import com.lyncode.jtwig.unit.resource.JtwigResource;
import com.lyncode.jtwig.unit.resource.StringJtwigResource;

import java.io.ByteArrayOutputStream;

public class AbstractJtwigTest {
    private JtwigParserBuilder parserBuilder = new JtwigParserBuilder();
    private JtwigContext context = new JtwigContext();
    private String output;

    protected String theResultOfRendering(JtwigTemplate template, JtwigContext context) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        template.compile(parserBuilder).render(new RenderStream(outputStream), context);
        return outputStream.toString();
    }

    protected String theResultOfRendering(JtwigTemplate template) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        template.compile(parserBuilder).render(new RenderStream(outputStream), context);
        return outputStream.toString();
    }

    protected JtwigTemplate theTemplate(String content) {
        return new JtwigTemplate(content);
    }

    protected JtwigParserBuilder theParser () {
        return this.parserBuilder;
    }

    protected JtwigContext theContext() {
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
        new JtwigTemplate(resource).compile(parserBuilder).render(new RenderStream(outputStream), theContext());
        this.output = outputStream.toString();
        return output;
    }
}
