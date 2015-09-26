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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.jtwig.Environment;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Compilable;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.api.ability.ElementList;
import org.jtwig.content.api.ability.ElementTracker;
import org.jtwig.content.api.ability.ExecutionAware;
import org.jtwig.extension.core.tokenparsers.model.Block;
import org.jtwig.extension.core.tokenparsers.model.Macro;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.extension.core.tokenparsers.model.SetVariable;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ParseException;
import org.jtwig.exception.RenderException;
import org.jtwig.exception.ResourceException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.loader.Loader;
import org.jtwig.loader.impl.EmptyLoader;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.jtwig.util.ArrayUtil;

public class Template implements Compilable, ElementList<Compilable>, ElementTracker<Compilable> {
    protected final JtwigPosition position;
    protected CompilableExpression parent;
    protected final Map<String, Block> blocks = new HashMap<>();
    protected final Map<String, Macro> macros = new HashMap<>();
    protected final Sequence content = new Sequence();
    
    public Template(final JtwigPosition position) {
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

    @Override
    public Template add(Compilable compilable) {
        if (compilable instanceof Sequence) {
            for (Compilable c : ((Sequence)compilable).elements()) {
                add(c);
            }
            return this;
        }
        
        if (parent != null) {
            if (compilable instanceof SetVariable) {
                content.add(compilable);
            }
            return this;
        }
        
        content.add(compilable);
        return this;
    }

    @Override
    public Collection<Compilable> elements() {
        return content.elements();
    }
    
    @Override
    public Template track(final Compilable compilable) {
        if (compilable instanceof Macro) {
            macros.put(((Macro)compilable).name(), (Macro)compilable);
        }
        if (compilable instanceof Block) {
            blocks.put(((Block)compilable).name(), (Block)compilable);
        }
        
        return this;
    }
    
    public Template setParent(final CompilableExpression parent) {
        this.parent = parent;
        return this;
    }
    
    public JtwigPosition getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"["+position.getResource()+"]";
    }

    //~ Compilable impl ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public Compiled compile(final CompileContext context) throws CompileException {
        Expression parentExpr = parent != null ? parent.compile(context) : null;
        return new Compiled(position,
                parentExpr,
                compileBlocks(context),
                compileMacros(context),
                content.compile(context),
                context);
    }
    
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
    
    public static class Compiled implements ExecutionAware, Renderable {
        protected final JtwigPosition position;
        protected final Expression parentExpr;
        protected final Map<String, Block.CompiledBlock> blocks;
        protected final Map<String, Macro.Compiled> macros;
        protected final Renderable content;
        protected final CompileContext compileContext;
        
        public Compiled(final JtwigPosition position,
                final Expression parentExpr,
                final Map<String, Block.CompiledBlock> blocks,
                final Map<String, Macro.Compiled> macros,
                final Renderable content,
                final CompileContext compileContext) {
            this.position = position;
            this.parentExpr = parentExpr;
            this.blocks = blocks;
            this.macros = macros;
            this.content = content;
            this.compileContext = compileContext;
        }
        
        public JtwigPosition getPosition() {
            return position;
        }
        
        @Override
        public void render(RenderContext context) throws RenderException {
            context.getTemplateStack().add(this);
            doRender(context);
            try {
                context.renderStream().waitForExecutorCompletion();
                context.renderStream().close();
                context.renderStream().merge();
            } catch (IOException ex) {
                throw new RenderException(ex);
            } finally {
                context.getTemplateStack().pop();
            }
        }
        protected void doRender(RenderContext context) throws RenderException {
            // In Twig, the renderable content is always executed before the
            // inheritance component
            content.render(context);
            if (parentExpr == null) {
                return;
            }
            
            try {
                // Now get the parent template
                Loader.Resource parentResource = resolveExtendedResource(parentExpr.calculate(context), context.environment());
                if (parentResource == null || parentResource instanceof EmptyLoader.NoResource) {
                    throw new ResourceException("Resource not found: "+parentExpr.calculate(context)+" from "+position.getResource().canonicalPath());
                }
                
                // Handle the context, replace the blocks, and render
                CompileContext localContext = compileContext.clone().withResource(parentResource);
                Template parentTemplate = context.environment().parse(parentResource);
                Template.Compiled compiledTemplate = parentTemplate.compile(localContext);
                compiledTemplate.render(context);
            } catch (CalculateException | CompileException | ParseException | ResourceException ex) {
                throw new RenderException(ex);
            }
        }
        
        public Block.CompiledBlock block(String name) {
            if (blocks.containsKey(name)) {
                return blocks.get(name);
            }
            return null;
        }

        public Map<String, Macro.Compiled> macros() {
            return macros;
        }
        public Macro.Compiled macro(String name) {
            if (macros.containsKey(name)) {
                return macros.get(name);
            }
            return null;
        }
    
        //~ ExecutionAware impl ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        @Override
        public Object execute(
                RenderContext ctx,
                String name,
                Object... parameters)
                throws RenderException {
            // Check for macros
            if (macros.containsKey(name)) {
                return macros.get(name).execute(ctx, null, parameters);
            }
            return null;
        }
        
        //~ Helpers ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        protected Loader.Resource resolveExtendedResource(Object obj, final Environment env) throws ResourceException {
            // If we've been given a collection, the first template found is
            // used
            if (obj.getClass().isArray()) {
                obj = ArrayUtil.toList(obj);
            }
            if (obj instanceof Collection) {
                Collection col = (Collection)obj;
                for (Object o : col) {
                    try {
                        String path = position.getResource().resolve(o.toString());
                        Loader.Resource extendedResource = env.load(path);
                        if (extendedResource != null && !(extendedResource instanceof EmptyLoader.NoResource)) {
                            return extendedResource;
                        }
                    } catch (ResourceException e) {}
                }
                throw new ResourceException("Resources "+obj+" could not be found");
            }
            if (obj instanceof String) {
                String path = position.getResource().resolve(obj.toString());
                return env.load(path);
            }
            throw new ResourceException("Invalid resource name: "+obj);
        }
    }
}