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

import java.util.ArrayList;
import java.util.List;
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
            Compiled compiled;
            if (templateExpression != null) {
                compiled = new Compiled(position, context.clone(), isolated);
            } else {
                JtwigResource resource = context.retrieve(relativePath);
                if (!resource.exists() && ignoreMissing) {
                    return new Missing();
                }
                context = context.clone().withResource(resource);
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
        templateExpression = pop;
        return this;
    }

    public static class Compiled implements Renderable {
        private static final String TEMPLATE_PLACEHOLDER = "%s";
        private final JtwigPosition position;
        private final boolean isolated;
        private Renderable withTemplate;
        private Expression withExpression = null;
        private Expression template = null;
        private CompileContext compileContext = null;
        private String relativePath;

        public Compiled(JtwigPosition position, Renderable withTemplate, boolean isolated) {
            this.withTemplate = withTemplate;
            this.position = position;
            this.isolated = isolated;
        }

        public Compiled(JtwigPosition position, CompileContext compileContext, boolean isolated) throws ParseException, CompileException {
            this.position = position;
            this.isolated = isolated;
            this.compileContext = compileContext;
        }

        public Compiled with (Expression expression) {
            withExpression = expression;
            return this;
        }

        public Compiled template(Expression compile) {
            this.template = compile;
            return this;
        }

        public Compiled relativePath(String relativePath) {
            this.relativePath = relativePath;
            return this;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            RenderContext usedContext = context;
            if (template != null) {
                try {
                    final Object calculate = template.calculate(context);
                    List<String> parameters = null;
                    if (calculate instanceof String) {
                        parameters = new ArrayList<>();
                        parameters.add((String) calculate);
                    }
                    if (calculate instanceof List) {
                        parameters = (List<String>) calculate;
                    }
                    if (parameters != null && relativePath.contains(TEMPLATE_PLACEHOLDER)) {
                        try {
                            CompileContext compileContext = this.compileContext.clone();
                            final JtwigResource resource = compileContext.retrieve(String.format(relativePath, parameters.toArray()));
                            compileContext = compileContext.withResource(resource);
                            withTemplate = new Compiled(position, compileContext.parse(resource).compile(compileContext), isolated);
                        } catch (ResourceException | ParseException | CompileException e) {
                            throw new RenderException(e);
                        }
                    } else throw new RenderException(": Include 'withpathparameters' must be given an existing variable (string or list) and use '" + TEMPLATE_PLACEHOLDER + "' as placeholders.");
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
                        withTemplate.render(usedContext.with((Map) calculate));
                    } else throw new RenderException(position+": Include 'with' must be given a map.");
                } catch (CalculateException e) {
                    throw new RenderException(e);
                }
            } else
                withTemplate.render(usedContext);
        }


    }
    
    public static class Missing implements Renderable {
        @Override
        public void render(RenderContext context) throws RenderException {}
    }
}
