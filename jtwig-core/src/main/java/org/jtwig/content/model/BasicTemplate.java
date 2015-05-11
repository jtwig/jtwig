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
package org.jtwig.content.model;

import java.util.Map;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.compilable.Block;
import org.jtwig.content.model.compilable.Macro;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;

public class BasicTemplate extends Template {
    
    public BasicTemplate(final JtwigPosition position) {
        super(position);
    }

    //~ Compilable impl ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public CompiledBasicTemplate compile(final CompileContext context)
            throws CompileException {
        return new CompiledBasicTemplate(
                position,
                compileBlocks(context),
                compileMacros(context),
                content.compile(context));
    }
    
    public static class CompiledBasicTemplate extends CompiledTemplate {

        public CompiledBasicTemplate(final JtwigPosition position,
                final Map<String, Block.CompiledBlock> blocks,
                final Map<String, Macro.Compiled> macros,
                final Renderable content) {
            super(position, blocks, macros, content);
        }

        @Override
        public void doRender(RenderContext context) throws RenderException {
            content.render(context);
        }
        
    }
    
}