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

import java.util.ArrayList;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.render.RenderContext;
import org.jtwig.types.Undefined;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import org.jtwig.content.model.compilable.Content;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.util.ArrayUtil;

public class For extends Content<For> {
    private final String key;
    private final String value;
    private final CompilableExpression collection;
    private Sequence elseContent;
    
    public For(String value, CompilableExpression collection) {
        this(null, value, collection);
    }
    public For(String key, String value, CompilableExpression collection) {
        this.key = key;
        this.value = value;
        this.collection = collection;
    }

    public For withElse (Sequence content) {
        this.elseContent = content.withParent(this);
        return this;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        Renderable elseContent = null;
        if(this.elseContent != null) {
            elseContent = this.elseContent.compile(context);
        }
        return new Compiled(super.compile(context), collection.compile(context), elseContent, key, value);
    }

    static class Compiled implements Renderable {
        private final Renderable iterationContent;
        private final Expression collection;
        private final Renderable elseContent;
        private final String key;
        private final String value;
        
        public Compiled(Renderable iterationContent, Expression collection, Renderable elseContent, String key, String value) {
            this.iterationContent = iterationContent;
            this.collection = collection;
            this.elseContent = elseContent;
            this.key = key;
            this.value = value;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            try {
                Object resolved = collection.calculate(context);
                if(resolved == null) {
                    renderElse(context);
                    return;
                }
                
                while(resolved instanceof Expression) {
                    resolved = ((Expression)resolved).calculate(context);
                }
                if(resolved instanceof Undefined) {
                    renderElse(context);
                    return;
                }
                
                if(resolved.getClass().isArray()) {
                    resolved = ArrayUtil.toList(resolved);
                }
                if(resolved instanceof Map) {
                    handleMap((Map)resolved, context);
                } else if(resolved instanceof Collection) {
                    handleCollection((Collection)resolved, context);
                } else {
                    throw new RenderException("Expecting a map as parameter for the loop but " + collection + " was given");
                }
            } catch (CalculateException ex) {
                throw new RenderException(ex);
            }
        }
        
        protected void handleMap(Map map, RenderContext context)
                throws RenderException {
            if(map.isEmpty()) {
                renderElse(context);
            }
            
            Loop loop = new Loop(map.size());
            context.with("loop", loop);
            int index = 0;
            for (Object k : map.keySet()) {
                loop.update(index++);

                if(key != null) {
                    context.with(key, k);
                }
                context.with(value, map.get(k));

                iterationContent.render(context);
            }
        }
        
        protected void handleCollection(
                Collection collection,
                RenderContext context)
                throws RenderException {
            if(collection.isEmpty()) {
                renderElse(context);
                return;
            }
            
            Loop loop = new Loop(collection.size());
            context.with("loop", loop);
            for(Object obj : collection) {
                if(key != null) {
                    context.with(key, loop.index);
                }
                context.with(value, obj);
                
                iterationContent.render(context);
                loop.index++;
            }
        }
        
        protected void renderElse(RenderContext context) throws RenderException {
            if(elseContent != null) {
                elseContent.render(context);
            }
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

        public int getIndex0() {
            return index;
        }
        
        public int getIndex() {
            return index + 1;
        }

        public int getRevindex0() {
            return length - index - 1;
        }
        
        public int getRevindex() {
            return length - index;
        }

        public boolean isFirst() {
            return index == 0;
        }

        public boolean isLast() {
            return index == length - 1;
        }
    }
}
