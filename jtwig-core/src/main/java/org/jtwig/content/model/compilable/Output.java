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
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.render.RenderContext;

import java.io.IOException;

import static org.jtwig.util.TwigTransformUtils.toTwig;

public class Output extends AbstractElement {
    private final CompilableExpression expression;

    public Output(CompilableExpression expression) {
        this.expression = expression;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        return new Compiled(expression.compile(context));
    }

    private static class Compiled implements Renderable {
        private final Expression expression;

        private Compiled(Expression expression) {
            this.expression = expression;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            try {
                Object calculate = expression.calculate(context);
                if (calculate != null) {
                    context.write(toTwig(calculate).getBytes(context.environment().getConfiguration().getCharset()));
                }
            } catch (IOException | CalculateException e) {
                throw new RenderException(e);
            }
        }
    }
}
