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
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.*;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;

import java.util.Map;

public class Include extends AbstractElement {
    private final String relativePath;
    private final JtwigPosition position;
    private CompilableExpression withExpression = null;
    private CompilableExpression templateExpression = null;
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
            if (this.templateExpression != null) {
                ignoreMissing = false;
            }
            JtwigResource resource = context.retrieve(relativePath);

            if (!resource.exists() && ignoreMissing) {
                return new Missing();
            }
            context = context.clone().withResource(resource);

            Compiled compiled;
            if (this.templateExpression != null) {
                compiled = new Compiled(position, context, resource, isolated);
            } else {
                compiled = new Compiled(position, context.parse(resource).compile(context), isolated);

            }
            if (withExpression != null)
                compiled.with(withExpression.compile(context));
            if (templateExpression != null) {
                compiled.template(templateExpression.compile(context));
                compiled.relativePath(relativePath);
            }
            return compiled;
        } catch (ResourceException | ParseException e) {
            throw new CompileException(e);
        }
    }

    public Include template(CompilableExpression pop) {
        this.templateExpression = pop;
        return this;
    }

    public static class Compiled implements Renderable {
        private static final String TEMPLATE_PLACEHOLDER = "%s";
        private final JtwigPosition position;
        private final boolean isolated;
        private JtwigResource jtwigResource;
        private Renderable renderable;
        private Expression withExpression = null;
        private Expression template = null;
        private CompileContext compileContext = null;
        private String relativePath;

        public Compiled(JtwigPosition position, Renderable renderable, boolean isolated) {
            this.renderable = renderable;
            this.position = position;
            this.isolated = isolated;
        }

        public Compiled(JtwigPosition position, CompileContext compileContext, JtwigResource jtwigResource, boolean isolated) throws ParseException, CompileException {
            this.position = position;
            this.isolated = isolated;
            if (jtwigResource.exists()) {
                this.renderable = new Compiled(position, compileContext.parse(jtwigResource).compile(compileContext), isolated);
            } else {
                this.compileContext = compileContext;
                this.jtwigResource = jtwigResource;
            }
        }

        public Compiled with (Expression expression) {
            this.withExpression = expression;
            return this;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            RenderContext usedContext = context;
            if (this.template != null) {
                try {
                    Object calculate = template.calculate(context);
                    if (calculate instanceof String && this.jtwigResource.toString().contains(TEMPLATE_PLACEHOLDER)) {
                        try {
                            JtwigResource resource = compileContext.retrieve(this.relativePath.replace(TEMPLATE_PLACEHOLDER, (String) calculate));
                            this.renderable = new Compiled(position, compileContext.parse(resource).compile(compileContext), isolated);
                        } catch (ResourceException | ParseException | CompileException e) {
                            throw new RenderException(e);
                        }
                    } else throw new RenderException(": Include 'template' must be given an existing variable and use '" + TEMPLATE_PLACEHOLDER + "' as placeholder.");
                } catch (CalculateException e) {
                    throw new RenderException(e);
                }
            }
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

        public Compiled template(Expression compile) {
            this.template = compile;
            return this;
        }

        public Compiled relativePath(String relativePath) {
            this.relativePath = relativePath;
            return this;
        }
    }
    
    public static class Missing implements Renderable {
        @Override
        public void render(RenderContext context) throws RenderException {}
    }
}
