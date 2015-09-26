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

package org.jtwig.extension.core.tokenparsers.model;

import org.apache.commons.lang3.StringUtils;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.exception.*;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import java.util.HashMap;
import java.util.Map;
import org.jtwig.content.model.Template;
import org.jtwig.content.model.compilable.AbstractElement;
import org.jtwig.expressions.model.Constant;
import org.jtwig.expressions.model.Variable;
import org.jtwig.loader.Loader;

public abstract class Import {
    /**
     * Represents the general import statement during execution.
     * {% import (_self|constant|expression) as name %}
     */
    public static class General extends AbstractElement {
        private final JtwigPosition position;
        private final CompilableExpression from;
        private final String as;
        
        public General(final JtwigPosition position, final CompilableExpression from, final String as) {
            this.position = position;
            this.from = from;
            this.as = as;
        }

        @Override
        public Renderable compile(final CompileContext context) throws CompileException {
            return new Compiled(position, context, from.compile(context), as);
        }
        
        /**
         * Pulls macros into the render context.
         */
        static class Compiled implements Renderable {
            private final JtwigPosition position;
            private final CompileContext compileContext;
            private final Expression from;
            private final String as;
            
            Compiled(final JtwigPosition position, final CompileContext compileContext, final Expression from, final String as) {
                this.position = position;
                this.compileContext = compileContext;
                this.from = from;
                this.as = as;
            }

            @Override
            public void render(RenderContext context) throws RenderException {
                try {
                    Template.Compiled template = getTemplate(from, position, compileContext, context);
                    context.with(as, template);
                } catch (CalculateException | CompileException | ParseException | ResourceException ex) {
                    throw new RenderException(ex);
                }
            }
            
        }
        
    }
    /**
     * Represents the from..import statement during execution.
     * {% from (_self|constant|expression) import name [as name]
     */
    public static class From extends AbstractElement {
        private final JtwigPosition position;
        private final CompilableExpression from;
        private final Map<String, String> imports = new HashMap<>();
        
        public From(final JtwigPosition position, final CompilableExpression from) {
            this.position = position;
            this.from = from;
        }
        
        public From add(final String source, final String as) {
            imports.put(source, as);
            return this;
        }

        @Override
        public Renderable compile(CompileContext context) throws CompileException {
            return new Compiled(position, context, from.compile(context), imports);
        }
        
        
        /**
         * Pulls macros into the render context.
         */
        static class Compiled implements Renderable {
            private final JtwigPosition position;
            private final CompileContext compileContext;
            private final Expression from;
            private Map<String, String> imports = new HashMap<>();
            
            Compiled(final JtwigPosition position, final CompileContext compileContext, final Expression from, final Map<String, String> imports) {
                this.position = position;
                this.compileContext = compileContext;
                this.from = from;
                this.imports = imports;
            }

            @Override
            public void render(final RenderContext context) throws RenderException {
                try {
                    Template.Compiled template = getTemplate(from, position, compileContext, context);
                    for (String name : imports.keySet()) {
                        Macro.Compiled macro = template.macro(name);
                        if (macro != null) {
                            context.with(StringUtils.defaultIfBlank(imports.get(name), name), macro);
                        }
                    }
                } catch (CalculateException | CompileException | ParseException | ResourceException ex) {
                    throw new RenderException(ex);
                }
            }
            
        }
    }
    /**
     * Preserves the reference to _self so that the resource in which the
     * reference was made is always known.
     */
    public static class SelfReference implements CompilableExpression, Expression {
        private final Loader.Resource resource;
        
        public SelfReference(final JtwigPosition position) {
            this.resource = position.getResource();
        }
        
        public Loader.Resource resource() {
            return resource;
        }

        @Override
        public Expression compile(final CompileContext context) throws CompileException {
            return this;
        }

        @Override
        public Object calculate(final RenderContext context) throws CalculateException {
            return "_self";
        }
    }
    
    protected static Template.Compiled getTemplate(
            final Expression from,
            final JtwigPosition position,
            final CompileContext compileContext,
            final RenderContext renderContext)
            throws ResourceException,
                    ParseException,
                    CompileException,
                    CalculateException {
        if ((from instanceof Variable && "_self".equals(((Variable)from).name()))
                || (from instanceof Constant && "_self".equals(((Constant)from).getValue()))
                || from instanceof SelfReference) {
            return position.getCompiledTemplate(renderContext);
        }
        String path = position.getResource().resolve((String)from.calculate(renderContext));
        Loader.Resource resource = compileContext.environment().load(path);
        return compileContext.environment().compile(resource);
    }
}