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

package org.jtwig.extension.core.tokenparsers.model;

import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.jtwig.content.api.ability.ExecutionAware;
import org.jtwig.content.model.compilable.Content;

public class Macro extends Content<Macro> {
    private static final Logger LOG = LoggerFactory.getLogger(Macro.class);
    private final JtwigPosition position;
    private final String name;
    private final List<String> arguments = new ArrayList<>();

    public Macro(JtwigPosition position, String name) {
        this.position = position;
        this.name = name;
    }

    public String name() {
        return name;
    }

    public Macro add(final String arg) {
        arguments.add(arg);
        return this;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        return new Compiled(name, arguments, super.compile(context));
    }
    
    public static class Compiled implements ExecutionAware, Renderable {
        private final String name;
        private final List<String> argumentNames;
        private final Renderable content;
        
        public Compiled(final String name, final List<String> argumentNames, final Renderable content) {
            this.name = name;
            this.argumentNames = argumentNames;
            this.content = content;
        }
        
        public List<String> arguments() {
            return argumentNames;
        }
        
        @Override
        public void render(RenderContext context) throws RenderException {
            content.render(context);
        }
        
        @Override
        public String execute(final RenderContext ctx, final String name, final Object...parameters) throws RenderException {
            return execute(ctx, Arrays.asList(parameters));
        }
        public String execute(final RenderContext ctx, final List<Object> parameters) throws RenderException {
            // Build the model
            RenderContext isolated = ctx.isolatedModel();
            ((Map)isolated.map("model")).clear();
            for (int i = 0; i < arguments().size(); i++) {
                if (parameters.size() > i) {
                    isolated.with(arguments().get(i), parameters.get(i));
                }
            }
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            render(isolated);
            return buf.toString();
        }
        
    }
}
