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

package org.jtwig.content.model.compilable;

import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;

public class Block extends Content<Block> {
    private final JtwigPosition position;
    private final String name;

    public Block(final JtwigPosition position, final String name) {
        this.position = position;
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public Renderable compile(final CompileContext context) throws CompileException {
        return new CompiledBlock(position, name(), super.compile(context));
    }
    
    public static class CompiledBlock implements Renderable {
        private final JtwigPosition position;
        private final String name;
        private final Renderable content;
        
        public CompiledBlock(final JtwigPosition position, final String name, final Renderable content) {
            this.position = position;
            this.name = name;
            this.content = content;
        }
        @Override
        public void render(final RenderContext context) throws RenderException {
            CompiledBlock b = position.getTemplate().getCachedCompiledTemplate().block(name);
            b.content.render(context);
        }
        
    }
}