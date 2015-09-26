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

import java.util.Map;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.Template;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;

public class Embed extends Template {
    private CompilableExpression template;

    public Embed(JtwigPosition position) {
        super(position);
    }
    
    public CompilableExpression getTemplate() {
        return template;
    }
    public Embed setTemplate(final CompilableExpression template) {
        this.template = template;
        return this;
    }
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
    
    public static class Compiled extends Template.Compiled {

        public Compiled(JtwigPosition position, Expression parentExpr, Map<String, Block.CompiledBlock> blocks, Map<String, Macro.Compiled> macros, Renderable content, CompileContext compileContext) {
            super(position, parentExpr, blocks, macros, content, compileContext);
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            context = context.isolatedModel()
                    .clearTemplateStack();
            super.render(context);
        }
        
    }
    
}
