/**
 *
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

package com.lyncode.jtwig.addons.concurrent;

import com.lyncode.jtwig.addons.Addon;
import com.lyncode.jtwig.addons.AddonModel;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;

import java.io.IOException;

public class ConcurrentAddon extends Addon {
    public ConcurrentAddon(JtwigResource resource, ParserConfiguration configuration) {
        super(resource, configuration);
    }

    @Override
    public AddonModel instance() {
        return new AddonModel() {
            @Override
            public Renderable compile(CompileContext context) throws CompileException {
                return new Compiled(super.compile(context));
            }
        };
    }

    @Override
    public String beginKeyword() {
        return "concurrent";
    }

    @Override
    public String endKeyword() {
        return "endconcurrent";
    }

    private static class Compiled implements Renderable {
        private final Renderable content;

        private Compiled(Renderable content) {
            this.content = content;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            try {
                context.renderConcurrent(content);
            } catch (IOException e) {
                throw new RenderException(e);
            }
        }
    }
}
