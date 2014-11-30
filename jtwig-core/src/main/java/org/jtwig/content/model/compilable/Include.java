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

public class Include extends AbstractElement {
    private final String relativePath;
    private final JtwigPosition position;
    private CompilableExpression withExpression = null;
    private boolean ignoreMissing = false;
    private boolean isolated = false;

    public Include(JtwigPosition position, String relativePath) {
        this.position = position;
        this.relativePath = relativePath;
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
        try {
            JtwigResource resource = context.retrieve(relativePath);
            if (!resource.exists() && ignoreMissing) {
                return new Missing();
            }
            context = context.clone().withResource(resource);

            Compiled compiled = new Compiled(position, context.parse(resource).compile(context), isolated);
            if (withExpression != null)
                compiled.with(withExpression.compile(context));
            return compiled;
        } catch (ResourceException | ParseException e) {
            throw new CompileException(e);
        }
    }

    public static class Compiled implements Renderable {
        private final Renderable renderable;
        private final JtwigPosition position;
        private final boolean isolated;
        private Expression withExpression = null;

        public Compiled(JtwigPosition position, Renderable renderable, boolean isolated) {
            this.renderable = renderable;
            this.position = position;
            this.isolated = isolated;
        }

        public Compiled with (Expression expression) {
            this.withExpression = expression;
            return this;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
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
            } else
                renderable.render(usedContext);
        }
    }
    
    public static class Missing implements Renderable {
        @Override
        public void render(RenderContext context) throws RenderException {}
    }
}
