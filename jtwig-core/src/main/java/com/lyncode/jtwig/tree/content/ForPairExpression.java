/**
 * Copyright 2012 Lyncode
 *
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

package com.lyncode.jtwig.tree.content;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.tree.api.Compilable;
import com.lyncode.jtwig.tree.api.Renderable;
import com.lyncode.jtwig.tree.structural.BlockExpression;
import com.lyncode.jtwig.tree.value.Variable;

import java.io.OutputStream;
import java.util.Map;

public class ForPairExpression implements Renderable, Compilable<ForPairExpression> {
    private Variable key;
    private Variable value;
    private Object map;
    private Content content;

    public ForPairExpression(Variable key, Variable value, Object map) {
        this.key = key;
        this.value = value;
        this.map = map;
    }

    public Variable getKey() {
        return key;
    }

    public Object getMap() {
        return map;
    }

    public Content getContent() {
        return content;
    }

    public boolean setContent(Content content) {
        this.content = content;
        return true;
    }

    @Override
    public boolean render(OutputStream outputStream, JtwigContext context) throws RenderException {
        try {
            Object resolved = context.resolve(map);

            if (!(resolved instanceof Map))
                throw new RenderException("Expecting a map as parameter for the loop but "+map+" was given");

            Map map = (Map) resolved;
            Loop loop = new Loop(map.size());
            context.set("loop", loop);
            int index = 0;
            for (Object key : map.keySet()) {
                loop.update(index++);
                context.set(this.key.getIdentifier(), key);
                context.set(this.value.getIdentifier(), map.get(key));
                content.render(outputStream, context);
            }
            return true;
        } catch (CalculateException e) {
            throw new RenderException(e);
        }
    }

    @Override
    public ForPairExpression compile(JtwigResource resource) throws CompileException {
        content = content.compile(resource);
        return this;
    }

    @Override
    public boolean replace(BlockExpression expression) throws CompileException {
        return content.replace(expression);
    }

    public String toString() {
        return "For each element of " + map + " render " + content;
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
