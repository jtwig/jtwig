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

import java.util.Collection;
import java.util.Map;
import org.jtwig.Environment;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Compilable;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.compilable.Block;
import org.jtwig.content.model.compilable.Comment;
import org.jtwig.content.model.compilable.Macro;
import org.jtwig.content.model.compilable.SetVariable;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ParseBypassException;
import org.jtwig.exception.ParseException;
import org.jtwig.exception.RenderException;
import org.jtwig.exception.ResourceException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.loader.Loader;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;

public class ExtendsTemplate extends Template {
    private CompilableExpression expr;
    
    public ExtendsTemplate(final JtwigPosition position) {
        super(position);
    }
    
    public ExtendsTemplate extend(final CompilableExpression expr) {
        this.expr = expr;
        return this;
    }

    //~ Template impl ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public Template add(final Compilable compilable) {
        if (compilable instanceof Comment) {
            // Discard
        } else if (compilable instanceof SetVariable) {
            content.add(compilable);
        } else {
            throw new ParseBypassException(new ParseException("Extending templates do not support "+compilable.getClass().getName()));
        }
        return this;
    }

    //~ Compilable impl ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public CompiledExtendsTemplate compile(final CompileContext context) throws CompileException {
        return new CompiledExtendsTemplate(
                position,
                expr.compile(context),
                compileBlocks(context),
                compileMacros(context),
                content.compile(context),
                context);
    }
    
    public static class CompiledExtendsTemplate extends CompiledTemplate {
        protected final Expression expr;
        protected final CompileContext compileContext;
        
        public CompiledExtendsTemplate(
                final JtwigPosition position,
                final Expression expr,
                final Map<String, Block.CompiledBlock> blocks,
                final Map<String, Macro.Compiled> macros,
                final Renderable content,
                final CompileContext compileContext) {
            super(position, blocks, macros, content);
            this.expr = expr;
            this.compileContext = compileContext;
        }

        @Override
        public void doRender(final RenderContext context) throws RenderException {
            // In Twig, the renderable content is always executed before the
            // inheritance component
            content.render(context);
            
            try {
                // Now get the template
                Loader.Resource extendedResource = resolveExtendedResource(expr.calculate(context), context.environment());
                if (extendedResource == null) {
                    throw new ResourceException("Resource not found");
                }
                
                // Handle the context, replace the blocks, and render
                CompileContext localContext = compileContext.clone().withResource(extendedResource);
                Template parsed = context.environment().parse(extendedResource);
                CompiledTemplate r = context.environment().compile(parsed, extendedResource, localContext);
                r.withChildTemplate(this);
                r.render(context);
            } catch (CalculateException | CompileException | ParseException | ResourceException ex) {
                throw new RenderException(ex);
            }
        }
        
        protected Loader.Resource resolveExtendedResource(final Object obj, final Environment env) throws ResourceException {
            // If we've been given a collection, the first template found is
            // used
            if (obj instanceof Collection) {
                Collection col = (Collection)obj;
                for (Object o : col) {
                    try {
                        String path = position.getResource().resolve(o.toString());
                        Loader.Resource extendedResource = env.load(path);
                        if (extendedResource != null) {
                            return extendedResource;
                        }
                    } catch (ResourceException e) {}
                }
            }
            if (obj instanceof String) {
                String path = position.getResource().resolve(obj.toString());
                return env.load(path);
            }
            throw new ResourceException("Invalid resource name: "+obj);
        }
        
    }
}