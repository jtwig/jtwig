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

package com.lyncode.jtwig.content.model.compilable;

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.expressions.model.Constant;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.render.config.RenderConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.util.ObjectExtractor;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

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
            return new Compiled(context, evaluateFrom(from, context).compile(context), as);
        }
        
        /**
         * Pulls macros into the render context.
         */
        static class Compiled implements Renderable {
            private final CompileContext compileContext;
            private final Expression from;
            private final String as;
            
            Compiled(final CompileContext compileContext, final Expression from, final String as) {
                this.compileContext = compileContext;
                this.from = from;
                this.as = as;
            }

            @Override
            public void render(RenderContext context) throws RenderException {
                try {
                    final JtwigResource template;
                    if (from instanceof SelfReference) {
                        template = ((SelfReference)from).resource();
                    } else {
                        template = compileContext.retrieve((String)from.calculate(context));
                    }
                    final Map<String, Macro.Compiled> macros = Import.getMacros(compileContext, template);
                    context.with(as, new MacroRepository(macros));
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
            return new Compiled(context, evaluateFrom(from, context).compile(context), imports);
        }
        
        
        /**
         * Pulls macros into the render context.
         */
        static class Compiled implements Renderable {
            private final CompileContext compileContext;
            private final Expression from;
            private Map<String, String> imports = new HashMap<>();
            
            Compiled(final CompileContext compileContext, final Expression from, final Map<String, String> imports) {
                this.compileContext = compileContext;
                this.from = from;
                this.imports = imports;
            }

            @Override
            public void render(final RenderContext context) throws RenderException {
                try {
                    final JtwigResource template;
                    if (from instanceof SelfReference) {
                        template = ((SelfReference)from).resource();
                    } else {
                        template = compileContext.retrieve((String)from.calculate(context));
                    }
                    final Map<String, Macro.Compiled> macros = Import.getMacros(compileContext, template);
                    for (String name : imports.keySet()) {
                        if (macros.containsKey(name)) {
                            context.with(StringUtils.defaultIfBlank(imports.get(name), name), macros.get(name));
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
        private final JtwigResource resource;
        
        public SelfReference(final JtwigPosition position) {
            this.resource = position.getResource();
        }
        
        public JtwigResource resource() {
            return resource;
        }

        @Override
        public Expression compile(final CompileContext context) throws CompileException {
            return this;
        }

        @Override
        public Object calculate(final RenderContext context) throws CalculateException {
            throw new UnsupportedOperationException();
        }
    }
    /**
     * The MacroRepository maps macro names to instances. Instances are
     * populated with macros that are retrieved through the use of the import
     * statement, and those instances are then added to the RenderContext, in
     * much the same way that Twig's macros are contained within a TwigTemplate
     * which is itself within the RenderContext.
     * 
     * Some fancy footwork is done in the {@link ObjectExtractor} in order to
     * get this to work.
     */
    public static class MacroRepository {
        protected final Map<String, Macro.Compiled> macros;
        
        protected MacroRepository() {
            this.macros = new HashMap<>();
        }
        public MacroRepository(final Map<String, Macro.Compiled> macros) {
            this.macros = macros;
        }
        
        public Object execute(final String name, final Object...parameters) throws RenderException {
            if (!macros.containsKey(name)) {
                return "";
            }
            Macro.Compiled macro = macros.get(name);
            // Build the model
            JtwigModelMap model = new JtwigModelMap();
            for (int i = 0; i < macro.arguments().size(); i++) {
                if (parameters.length > i) {
                    model.put(macro.arguments().get(i), parameters[i]);
                }
            }
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            RenderContext rc = RenderContext.create(new RenderConfiguration(), model, buf);
            macro.render(rc);
            return buf.toString();
        }
    }
    
    /**
     * Retrieves macros from the given context for the given template. Resides
     * here due to common usage between implementation classes.
     * @param ctx
     * @param template
     * @return
     * @throws ResourceException
     * @throws ParseException
     * @throws CompileException 
     */
    protected static Map<String, Macro.Compiled> getMacros(final CompileContext ctx, final JtwigResource source) throws ResourceException, ParseException, CompileException {
        if (ctx.macros(source) != null) {
            return ctx.macros(source);
        }

        // Load the file
        ctx.parse(source).compile(ctx);
        if (ctx.macros(source) != null) {
            return ctx.macros(source);
        }
        return new HashMap<>();
    }
    /**
     * Evaluates the source template name/expression passed and tries to boil
     * it down to something simpler for render time. If the source template name
     * is a constant or a self reference, it loads the template macros ahead of
     * time.
     * @param from
     * @param ctx
     * @return
     * @throws CompileException 
     */
    protected static CompilableExpression evaluateFrom(CompilableExpression from, final CompileContext ctx) throws CompileException {
        // Keep parsing within the actual parse-phase where possible
        if (from instanceof Constant) {
            final String template = ((Constant)from).getValue().toString();
            try {
                ctx.parse(ctx.retrieve(template));
            } catch (ParseException | ResourceException ex) {
                throw new CompileException(ex);
            }
        }
        return from;
    }

}