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

package org.jtwig.expressions.model;

import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jtwig.content.model.Template;


public class BlockFunction extends AbstractCompilableExpression {
    private final List<CompilableExpression> arguments = new ArrayList<>();

    public BlockFunction(JtwigPosition position) {
        super(position);
    }

    public BlockFunction add(CompilableExpression argument) {
        arguments.add(argument);
        return this;
    }

    @Override
    public Expression compile(CompileContext context) throws CompileException {
        if (arguments.isEmpty()) {
            throw new CompileException("Block function requires a single argument");
        }
        List<Expression> compiledArguments = new ArrayList<>();
        for (CompilableExpression argument : arguments) {
            compiledArguments.add(argument.compile(context));
        }
        return new Compiled(position(), compiledArguments, context);
    }

    public static class Compiled implements Expression {
        private final JtwigPosition position;
        private final List<Expression> arguments;
        private final CompileContext compileContext;

        public Compiled(JtwigPosition position, List<Expression> arguments, CompileContext compileContext) {
            this.position = position;
            this.arguments = new CopyOnWriteArrayList<>(arguments);
            this.compileContext = compileContext;
        }

        @Override
        public Object calculate(RenderContext context) throws CalculateException {
            // Clone the RenderContext so that we can isolate the renderstream
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                RenderContext isolated = context.newRenderContext(baos);
                Template.CompiledTemplate template = context.getRenderingTemplate();
                Renderable block = template.getPrimordial().block(getFirstArgument().calculate(isolated).toString());
                if (block != null) {
                    block.render(isolated);
                }
                return baos.toString();
            } catch (RenderException e) {
                throw new CalculateException("Unable to render the block.", e);
            }
        }

        public Expression getFirstArgument() {
            return this.arguments.get(0);
        }
    }
}
