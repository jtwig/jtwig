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
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.render.RenderContext;

import java.io.IOException;

import static com.lyncode.jtwig.util.TwigTransformUtils.toTwig;

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
                if (calculate != null)
                    context.write(toTwig(calculate).getBytes());
            } catch (IOException | CalculateException e) {
                throw new RenderException(e);
            }
        }
    }
}
