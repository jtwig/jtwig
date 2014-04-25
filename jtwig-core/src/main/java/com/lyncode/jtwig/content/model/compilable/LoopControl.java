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
import com.lyncode.jtwig.functions.util.ObjectIterator;
import com.lyncode.jtwig.render.RenderContext;

public class LoopControl extends Content<LoopControl> {
    private final String variable;
    private final CompilableExpression collection;

    public LoopControl(String variable, CompilableExpression collection) {
        this.variable = variable;
        this.collection = collection;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        return new Compiled(super.compile(context), collection.compile(context), variable);
    }

    static class Compiled implements Renderable {
        private final String variable;
        private final Expression collection;
        private final Renderable content;

        public Compiled(Renderable content, Expression collection, String variable) {
            this.variable = variable;
            this.collection = collection;
            this.content = content;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            ObjectIterator iterator = null;
            try {
                iterator = new ObjectIterator(collection.calculate(context));
                Loop loop = new Loop(iterator.size());
                context.model().set("loop", loop);
                int index = 0;
                while (iterator.hasNext()) {
                    loop.update(index++);
                    Object object = iterator.next();
                    context.model().set(variable, object);
                    content.render(context);
                }
            } catch (CalculateException e) {
                throw new RenderException(e);
            }
        }

        protected Expression expression() {
            return collection;
        }

        protected String variable() {
            return variable;
        }

        protected Renderable content() {
            return content;
        }
    }

    public static class Loop {
        private int index = 0;
        private int length;

        public Loop(int length) {
            this.length = length;
        }

        public void update(int index) {
            this.index = index;
        }

        public int getLength() {
            return length;
        }

        public int getIndex() {
            return index;
        }

        public int getRevindex() {
            return length - index - 1;
        }

        public boolean isFirst() {
            return index == 0;
        }

        public boolean isLast() {
            return index == length - 1;
        }
    }
}
