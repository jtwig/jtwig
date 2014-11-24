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
import com.lyncode.jtwig.content.api.Compilable;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseBypassException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.expressions.model.Constant;
import com.lyncode.jtwig.expressions.model.OperationBinary;
import com.lyncode.jtwig.expressions.model.Variable;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.render.config.RenderConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.types.Undefined;
import com.lyncode.jtwig.util.ObjectExtractor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Import extends AbstractElement {
    private final JtwigPosition position;
    private CompilableExpression from;
    private Map<CompilableExpression, CompilableExpression> imports = new HashMap<>();
    
    public Import(final JtwigPosition position) {
        this.position = position;
    }
    
    public Import from(final CompilableExpression from) {
        this.from = from;
        return this;
    }
    public CompilableExpression from() {
        return from;
    }
    public Import add(final Definition def) {
        if (from == null && imports.size() > 0) {
            throw new ParseBypassException(new ParseException("End of statement block expected."));
        }
        if (from == null && def.as == null) {
            throw new ParseBypassException(new ParseException("Must specify 'as'"));
        }
        
        imports.put(def.source, def.as);
        return this;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        Map<Expression, Expression> imports = new HashMap<>();
        for (Map.Entry<CompilableExpression, CompilableExpression> entry : this.imports.entrySet()) {
            Expression name = entry.getKey().compile(context);
            if (name instanceof Variable.Compiled) {
                name = new Constant<>(((Variable.Compiled)name).name()).compile(context);
            }
            Expression newName = entry.getValue() == null ? name : entry.getValue().compile(context);
            if (newName instanceof Variable.Compiled) {
                newName = new Constant<>(((Variable.Compiled)newName).name()).compile(context);
            }
            imports.put(name, newName);
        }
        
        Expression from = null;
        if (this.from != null) {
            from = this.from.compile(context);
        }
        
        return new Compiled(context, from, imports);
    }
    
    static class Compiled implements Renderable {
        private final CompileContext context;
        private final Expression from;
        private final Map<Expression, Expression> imports;
        
        public Compiled(CompileContext context, Expression from, Map<Expression, Expression> imports) {
            this.context = context;
            this.from = from;
            this.imports = imports;
        }

        @Override
        public void render(final RenderContext context) throws RenderException {
            try {
                if (from != null) {
                    from(context);
                } else {
                    imprt(context);
                }
            } catch (CalculateException | CompileException | IOException | ParseException | ResourceException e) {
                throw new RenderException(e);
            }
        }
        
        protected void from(final RenderContext ctx) throws CalculateException, IOException, ResourceException, ParseException, CompileException {
            final String template = (String)from.calculate(ctx);
            final Map<String, Macro.Compiled> macros = getMacros(template);
            for (Expression key : imports.keySet()) {
                final String name = key.calculate(ctx).toString();
                final String newName = imports.get(key).calculate(ctx).toString();
                System.out.println("Importing macro '"+name+"' from "+template+" to name '"+newName+"'");
                if (macros.containsKey(name)) {
                    ctx.with(newName, macros.get(name));
                }
            }
        }
        
        protected void imprt(final RenderContext ctx) throws CalculateException, IOException, ResourceException, ParseException, CompileException {
            final Map.Entry<Expression, Expression> imprt = imports.entrySet().iterator().next();
            final String template = (String)imprt.getKey().calculate(ctx);
            final Map<String, Macro.Compiled> macros = getMacros(template);
            ctx.with((String)imprt.getValue().calculate(ctx), new MacroRepository(macros));
        }
        
        protected Map<String, Macro.Compiled> getMacros(final String template) throws ResourceException, ParseException, CompileException {
            final JtwigResource source = this.context.retrieve(template);
            Map<String, Macro.Compiled> macros = this.context.macros(source);
            if (macros != null) {
                return macros;
            }
            
            // Load the file
            this.context.parse(source).compile(context);
            macros = this.context.macros(source);
            if (macros != null) {
                return macros;
            }
            return new HashMap<>();
        }
        
    }
    
    /**
     * The definition is used to build the `name as name` statements during the
     * parsing phase.
     */
    public static class Definition implements Compilable {
        private final CompilableExpression source;
        private CompilableExpression as;
        
        public Definition(final CompilableExpression source) {
            this.source = source;
        }
        
        public Definition as(final CompilableExpression as) {
            if (as instanceof Variable) {
                this.as = new Constant<>(((Variable)as).name());
            } else {
                this.as = as;
            }
            return this;
        }
        
        public CompilableExpression source() {
            return source;
        }
        
        public CompilableExpression as() {
            return as;
        }

        @Override
        public Renderable compile(CompileContext context) throws CompileException {
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
}