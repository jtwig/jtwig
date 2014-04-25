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

import java.util.Map;

public class MapLoopControl extends Content {
    private final String key;
    private final String value;
    private final CompilableExpression collection;

    public MapLoopControl(String key, String value, CompilableExpression collection) {
        this.key = key;
        this.value = value;
        this.collection = collection;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        return new Compiled(super.compile(context), collection.compile(context), key, value);
    }

    public class Compiled extends LoopControl.Compiled {
        private final String value;

        public Compiled(Renderable content, Expression collection, String key, String value) {
            super(content, collection, key);
            this.value = value;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            try {
                Object resolved = expression().calculate(context);

                if (!(resolved instanceof Map)) {
                    throw new RenderException("Expecting a map as parameter for the loop but " + expression() + " was given");
                }

                Map map = (Map) resolved;
                LoopControl.Loop loop = new LoopControl.Loop(map.size());
                context.model().set("loop", loop);
                int index = 0;
                for (Object key : map.keySet()) {
                    loop.update(index++);

                    context.model().set(variable(), key);
                    context.model().set(value, map.get(key));

                    content().render(context);
                }
            } catch (CalculateException e) {
                throw new RenderException(e);
            }
        }
    }
}
