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
import com.lyncode.jtwig.functions.util.ObjectIterator;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.tree.api.Content;
import com.lyncode.jtwig.tree.api.Expression;
import com.lyncode.jtwig.tree.api.Tag;
import com.lyncode.jtwig.tree.api.TagInformation;
import com.lyncode.jtwig.tree.expressions.Variable;

import java.io.OutputStream;

public class ForLoop implements Content, Tag {
    protected Variable variable;
    protected JtwigContent content;
    protected Expression expression;
    protected TagInformation begin = new TagInformation();
    protected TagInformation end = new TagInformation();

    public ForLoop(Variable variable, Expression list) {
        this.variable = variable;
        this.expression = list;
    }


    public boolean setContent(JtwigContent content) {
        this.content = content;
        return true;
    }


    @Override
    public boolean render(OutputStream outputStream, JtwigContext context) throws RenderException {
        try {
            ObjectIterator iterator = new ObjectIterator(expression.calculate(context));
            Loop loop = new Loop(iterator.size());
            context.set("loop", loop);
            int index = 0;
            while (iterator.hasNext()) {
                loop.update(index++);
                Object object = iterator.next();
                context.set(variable.getIdentifier(), object);
                content.render(outputStream, context);
            }
            return true;
        } catch (CalculateException e) {
            throw new RenderException(e);
        }
    }

    @Override
    public Content compile(JtwigParser parser, JtwigResource resource) throws CompileException {
        content = content.compile(parser, resource, begin(), end());
        return this;
    }

    @Override
    public boolean replace(Content expression) throws CompileException {
        return content.replace(expression);
    }

    public String toString() {
        return "For each element of " + expression + " render " + content;
    }

    @Override
    public TagInformation begin() {
        return this.begin;
    }

    @Override
    public TagInformation end() {
        return this.end;
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
