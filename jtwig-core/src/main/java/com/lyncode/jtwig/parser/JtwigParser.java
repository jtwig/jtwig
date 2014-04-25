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

package com.lyncode.jtwig.parser;

import com.lyncode.jtwig.addons.AddonParser;
import com.lyncode.jtwig.addons.concurrent.ConcurrentParser;
import com.lyncode.jtwig.addons.filter.FilterParser;
import com.lyncode.jtwig.addons.spaceless.SpacelessParser;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Compilable;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.parser.parboiled.JtwigContentParser;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JtwigParser {
    private final ParserConfiguration configuration;
    private final List<Class<? extends AddonParser>> addons = new ArrayList<>();

    public JtwigParser(ParserConfiguration configuration) {
        this.configuration = configuration;

        this
                .withAddonParser(SpacelessParser.class)
                .withAddonParser(FilterParser.class)
                .withAddonParser(ConcurrentParser.class)
        ;
    }

    public JtwigParser() {
        this(new ParserConfiguration());
    }

    public JtwigParser withAddonParser(Class<? extends AddonParser> addonParser) {
        this.addons.add(addonParser);
        return this;
    }

    public Compilable parse(JtwigResource resource) throws ParseException {
        JtwigContentParser parser = JtwigContentParser
                .newParser(resource, configuration, addons);

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
