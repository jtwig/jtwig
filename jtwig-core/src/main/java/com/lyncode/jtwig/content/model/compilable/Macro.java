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

package com.lyncode.jtwig.content.model.compilable;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.render.config.RenderConfiguration;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Macro extends Content<Macro> {
    private static final Logger LOG = LoggerFactory.getLogger(Macro.class);
    private final JtwigPosition position;
    private final String name;
    private List<String> arguments = new ArrayList<>();

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
        context.withMacro(position.getResource(), name, new Compiled(name, arguments, super.compile(context)));
        
        return new Renderable() {
            @Override
            public void render(RenderContext context) throws RenderException {}
        };
    }
    
    public static class Compiled implements Renderable {
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
        
        public String execute(final Object...parameters) throws IOException, RenderException {
            return execute(Arrays.asList(parameters));
        }
        public String execute(final List<Object> parameters) throws IOException, RenderException {
            // Build the model
            JtwigModelMap model = new JtwigModelMap();
            for (int i = 0; i < arguments().size(); i++) {
                if (parameters.size() > i) {
                    model.put(arguments().get(i), parameters.get(i));
                }
            }
            try (ByteArrayOutputStream buf = new ByteArrayOutputStream()) {
                RenderContext rc = RenderContext.create(new RenderConfiguration(), model, buf);
                render(rc);
                return buf.toString();
            }
        }
        
    }
}