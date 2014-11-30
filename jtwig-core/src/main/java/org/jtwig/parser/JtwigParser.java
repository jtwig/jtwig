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

package org.jtwig.parser;

import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Compilable;
import org.jtwig.content.api.Renderable;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ParseException;
import org.jtwig.exception.RenderException;
import org.jtwig.parser.config.ParserConfiguration;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.render.RenderContext;
import org.jtwig.resource.JtwigResource;

import java.io.IOException;

public class JtwigParser {
    private final ParserConfiguration configuration;

    public JtwigParser(ParserConfiguration configuration) {
        this.configuration = configuration;
    }

    public Compilable parse(JtwigResource resource) throws ParseException {
        JtwigContentParser parser = JtwigContentParser
                .newParser(resource, configuration);

        return new Document(JtwigContentParser.parse(parser, resource));
    }

    private static class Document implements Compilable {
        private final Compilable content;

        private Document(Compilable content) {
            this.content = content;
        }

        @Override
        public Renderable compile(CompileContext context) throws CompileException {
            return new CompiledDocument(content.compile(context));
        }
    }

    private static class CompiledDocument implements Renderable {
        private final Renderable renderable;

        private CompiledDocument(Renderable renderable) {
            this.renderable = renderable;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            try {
                renderable.render(context);
                context.renderStream().waitForExecutorCompletion();
                context.renderStream().close();
                context.renderStream().merge();
            } catch (IOException e) {
                throw new RenderException(e);
            }
        }
    }
}
