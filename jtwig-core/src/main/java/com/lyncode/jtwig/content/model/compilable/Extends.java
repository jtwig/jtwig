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

import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Compilable;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Extends extends AbstractElement {
    private final String resourcePath;
    private final CompilableExpression expr;
    private final List<Block> blocks = new ArrayList<>();

    public Extends(CompilableExpression expr) {
        this.resourcePath = null;
        this.expr = expr;
    }
    public Extends(String resourcePath) {
        this.resourcePath = resourcePath;
        this.expr = null;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        try {
            if (expr != null) {
                return new Compiled(expr.compile(context), context, blocks);
            }
            if (resourcePath != null) {
                JtwigResource extendResource = context.retrieve(resourcePath);
                CompileContext newContext = context.clone().withResource(extendResource);

                for (Block block : blocks)
                    newContext.withReplacement(block.name(), block.compile(context));

                Compilable parsed = newContext.parse(extendResource);
                return parsed.compile(newContext);
            }
            throw new CompileException("Invalid extends syntax. Neither string or expression resource path provided");
        } catch (ParseException | ResourceException e) {
            throw new CompileException(e);
        }
    }

    public Extends add(Block block) {
        this.blocks.add(block);
        return this;
    }

    public static class Compiled implements Renderable {
        private final Expression resourcePathExpression;
        private final CompileContext globalContext;
        private final List<Block> blocks;

        public Compiled(Expression resourcePathExpression,
                CompileContext globalContext, List<Block> blocks) {
            this.resourcePathExpression = resourcePathExpression;
            this.globalContext = globalContext;
            this.blocks = blocks;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            try {
                Object obj = resourcePathExpression.calculate(context);
                
                // Resolve the resource to extend
                JtwigResource extendedResource = null;
                if (obj instanceof Collection) {
                    Collection col = (Collection)obj;
                    for (Object o : col) {
                        if ((extendedResource = find(o.toString())) != null) {
                            break;
                        }
                    }
                } else if (obj instanceof String) {
                    extendedResource = globalContext.retrieve(obj.toString());
                }
//                throw new RuntimeException("Type: "+obj.getClass().getCanonicalName());
                
                // Ensure that we actually have a resource
                if (extendedResource == null) {
                    throw new ResourceException("No resource found for extends tag");
                }
                
                // Handle the context, replace the blocks, and render
                CompileContext localContext = globalContext.clone().withResource(extendedResource);
                for (Block block : blocks)
                    localContext.withReplacement(block.name(), block.compile(globalContext));
                Compilable parsed = localContext.parse(extendedResource);
                Renderable r = parsed.compile(localContext);
                r.render(context);
            } catch (CalculateException | CompileException | ParseException | ResourceException ex) {
                throw new RenderException(ex);
            }
        }
        
        protected JtwigResource find(final String name) {
            try {
                return globalContext.retrieve(name);
            } catch (ResourceException ex) {}
            return null;
        }
    }
}
