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

package org.jtwig.extension.core.tokenparsers.model;

import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.compilable.AbstractElement;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.render.RenderContext;

public class SetVariable extends AbstractElement {
    private final String variable;
    private final CompilableExpression assignment;

    public SetVariable(String variable, CompilableExpression assignment) {
        this.variable = variable;
        this.assignment = assignment;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        return new Compiled(variable, assignment.compile(context));
    }

    private static class Compiled implements Renderable {
        private final String variable;
        private final Expression assignment;

        private Compiled(String variable, Expression assignment) {
            this.variable = variable;
            this.assignment = assignment;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            try {
                context.with(variable, assignment.calculate(context));
            } catch (CalculateException e) {
                throw new RenderException(e);
            }
        }
    }
}
