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

    public Include(JtwigPosition position, String relativePath) {
        this.position = position;
        this.relativePath = relativePath;
    }

    public Include with (CompilableExpression with) {
        this.withExpression = with;
        return this;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        try {
            JtwigResource resource = context.retrieve(relativePath);
            context = context.clone().withResource(resource);

            Compiled compiled = new Compiled(position, context.parse(resource).compile(context));
            if (withExpression != null)
                compiled.with(withExpression.compile(context));
            return compiled;
        } catch (ResourceException | ParseException e) {
            throw new CompileException(e);
        }
    }

    public static class Compiled implements Renderable {
        private final Renderable renderable;
        private final JtwigPosition position;
        private Expression withExpression = null;

        public Compiled(JtwigPosition position, Renderable renderable) {
            this.renderable = renderable;
            this.position = position;
        }

        public Compiled with (Expression expression) {
            this.withExpression = expression;
            return this;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            if (withExpression != null) {
                try {
                    Object calculate = withExpression.calculate(context);
                    if (calculate instanceof Map) {
                        renderable.render(context.isolatedModel().with((Map) calculate));
                    } else throw new RenderException(position+": Include 'with' must be given a map.");
                } catch (CalculateException e) {
                    throw new RenderException(e);
                }
            } else
                renderable.render(context);
        }
    }
}
