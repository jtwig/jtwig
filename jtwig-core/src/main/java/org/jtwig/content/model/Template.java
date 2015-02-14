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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Compilable;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.api.ability.ElementList;
import org.jtwig.content.api.ability.ElementTracker;
import org.jtwig.content.api.ability.ExecutionAware;
import org.jtwig.content.model.compilable.Block;
import org.jtwig.content.model.compilable.Macro;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;

public abstract class Template implements Compilable, ElementList<Compilable>, ElementTracker<Compilable> {
    protected final JtwigPosition position;
    protected final Map<String, Block> blocks = new HashMap<>();
    protected final Map<String, Macro> macros = new HashMap<>();
    protected final Sequence content = new Sequence();
    
    protected Template(final JtwigPosition position) {
        this.position = position;
    }
    
    //~ Local impl ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Map<String, Block> blocks() {
        return blocks;
    }
    public Block block(final String name) {
        if (blocks.containsKey(name)) {
            return blocks.get(name);
        }
        return null;
    }
    public Sequence content() {
        return content;
    }
    
    @Override
    public Template add(Compilable compilable) {
        content.add(compilable);
        return this;
    }
    
    @Override
    public Template track(final Compilable compilable) {
        if (compilable instanceof Macro) {
            macros.put(((Macro)compilable).name(), (Macro)compilable);
            return this;
        }
        if (compilable instanceof Block) {
            blocks.put(((Block)compilable).name(), (Block)compilable);
        }
        
        return this;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"["+position.getResource()+"]";
    }

    //~ Compilable impl ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public abstract CompiledTemplate compile(final CompileContext context) throws CompileException;
    
    //~ Helpers ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    protected Map<String, Macro.Compiled> compileMacros(
            final CompileContext ctx)
            throws CompileException {
        Map<String, Macro.Compiled> result = new HashMap<>();
        for (String key : macros.keySet()) {
            result.put(key, (Macro.Compiled)macros.get(key).compile(ctx));
        }
        return result;
    }
    protected Map<String, Block.CompiledBlock> compileBlocks(
            final CompileContext ctx)
            throws CompileException {
        Map<String, Block.CompiledBlock> result = new HashMap<>();
        for (String key : blocks.keySet()) {
            result.put(key, (Block.CompiledBlock)blocks.get(key).compile(ctx));
        }
        return result;
    }
    
    public static abstract class CompiledTemplate implements ExecutionAware, Renderable {
        protected final JtwigPosition position;
        protected final Map<String, Block.CompiledBlock> blocks;
        protected final Map<String, Macro.Compiled> macros;
        protected final Renderable content;
        protected CompiledTemplate child;
        protected CompiledTemplate parent;
        
        public CompiledTemplate(final JtwigPosition position,
                final Map<String, Block.CompiledBlock> blocks,
                final Map<String, Macro.Compiled> macros,
                final Renderable content) {
            this.position = position;
            this.blocks = blocks;
            this.macros = macros;
            this.content = content;
        }
        
        public CompiledTemplate withChildTemplate(final CompiledTemplate child) {
            this.child = child;
            child.parent = this;
            return this;
        }
        public CompiledTemplate withParentTemplate(final CompiledTemplate parent) {
            this.parent = parent;
            parent.child = this;
            return this;
        }
        
        @Override
        public void render(final RenderContext context) throws RenderException {
            context.pushRenderingTemplate(this);
            doRender(context);
            try {
                context.renderStream().waitForExecutorCompletion();
                context.renderStream().close();
                context.renderStream().merge();
            } catch (IOException ex) {
                throw new RenderException(ex);
            } finally {
                context.popRenderingTemplate();
            }
        }
        protected abstract void doRender(RenderContext context) throws RenderException;
        
        //~ Hierarchy mgmt ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        public CompiledTemplate getPrimordial() {
            if (parent != null) {
                return parent.getPrimordial();
            }
            return this;
        }
        
        //~ Block mgmt ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        public Map<String, Block.CompiledBlock> blocks() {
            return blocks;
        }
        public Block.CompiledBlock block(final String name) {
            if (child != null) {
                Block.CompiledBlock block = child.block(name);
                if (block != null) {
                    return block;
                }
            }
            
            if (blocks.containsKey(name)) {
                return blocks.get(name);
            }
            return null;
        }
        public Block.CompiledBlock parentBlock(final String name) {
            if (blocks.containsKey(name)) {
                return blocks.get(name);
            }
            
            if (parent != null) {
                Block.CompiledBlock block = parent.parentBlock(name);
                if (block != null) {
                    return block;
                }
            }
            
            return null;
        }
        
        public Map<String, Macro.Compiled> macros() {
            return macros;
        }
        public Macro.Compiled macro(final String name) {
            if (macros.containsKey(name)) {
                return macros.get(name);
            }
            return null;
        }
    
        //~ ExecutionAware impl ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        @Override
        public Object execute(
                final RenderContext ctx,
                final String name,
                final Object... parameters)
                throws RenderException {
            // Check for macros
            if (macros.containsKey(name)) {
                return macros.get(name).execute(ctx, null, parameters);
            }
            return null;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName()+"["+position.getResource()+"]";
        }
    }
}