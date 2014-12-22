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
import org.jtwig.exception.*;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.jtwig.resource.JtwigResource;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jtwig.expressions.model.Constant;

public class Include extends AbstractElement {
    private final CompilableExpression expr;
    private final JtwigPosition position;
    private CompilableExpression withExpression = null;
    private boolean ignoreMissing = false;
    private boolean isolated = false;

    public Include(JtwigPosition position, CompilableExpression expr) {
        this.position = position;
        this.expr = expr;
    }

    public Include with (CompilableExpression with) {
        this.withExpression = with;
        return this;
    }
    
    public Include setIsolated(boolean isolated) {
        this.isolated = isolated;
        return this;
    }
    
    public Include setIgnoreMissing (boolean ignoreMissing) {
        this.ignoreMissing = ignoreMissing;
        return this;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        // If we've been given a constant, let's try and grab the template and
        // make sure it exists
        try {
            if (expr instanceof Constant) {
                JtwigResource resource = context.retrieve(((Constant)expr).getValue().toString());
                if (!resource.exists() && !ignoreMissing) {
                    resource.retrieve();
                }
            }
        } catch (ResourceException ex) {
            throw new CompileException(ex);
        }
        
        Compiled compiled = new Compiled(position, expr.compile(context), isolated, ignoreMissing, context);
        if (withExpression != null)
            compiled.with(withExpression.compile(context));
        return compiled;
    }

    public static class Compiled implements Renderable {
        private final Expression expr;
        private final JtwigPosition position;
        private final boolean isolated;
        private final boolean ignoreMissing;
        private final CompileContext compileContext;
        private Expression withExpression = null;

        public Compiled(JtwigPosition position, Expression expr,
                boolean isolated, boolean ignoreMissing,
                CompileContext compileContext) {
            this.expr = expr;
            this.position = position;
            this.isolated = isolated;
            this.ignoreMissing = ignoreMissing;
            this.compileContext = compileContext;
        }

        public Compiled with (Expression expression) {
            this.withExpression = expression;
            return this;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            try {
                // Build the renderable
                JtwigResource resource = compileContext.retrieve(expr.calculate(context).toString());
                if (!resource.exists() && ignoreMissing) {
                    return;
                }
                CompileContext compileCtx = compileContext.clone().withResource(resource);
                Renderable renderable = compileCtx.parse(resource).compile(compileCtx);

                // Isolate the render context if needed
                RenderContext usedContext = context;
                if (isolated) {
                    usedContext = context.isolatedModel();
                    ((Map)usedContext.map("model")).clear();
                }

                if (withExpression != null) {
                    try {
                        Object calculate = withExpression.calculate(context);
                        if (calculate instanceof Map) {
                            renderable.render(usedContext.with((Map) calculate));
                        } else throw new RenderException(position+": Include 'with' must be given a map.");
                    } catch (CalculateException e) {
                        throw new RenderException(e);
                    }
                } else {
                    renderable.render(usedContext);
                }
            } catch (CalculateException | CompileException | ParseException | ResourceException ex) {
                throw new RenderException(ex);
            }
        }
    }
    
    public static class Missing implements Renderable {
        @Override
        public void render(RenderContext context) throws RenderException {}
    }
}
