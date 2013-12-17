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
import com.lyncode.jtwig.exception.ComposeException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.tree.api.Composable;
import com.lyncode.jtwig.tree.api.Renderable;
import com.lyncode.jtwig.tree.structural.BlockExpression;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.lyncode.jtwig.util.BooleanOperations.isTrue;

public class IfExpression implements Renderable, Composable<IfExpression> {
    private Object conditionalExpression;
    private Content content;
    private ElseExpression elseExpression = null;
    private List<ElseIfExpression> elseIfExpressions = new ArrayList<ElseIfExpression>();

    public IfExpression(Object conditionalExpression) {
        this.conditionalExpression = conditionalExpression;
    }

    public boolean setElseExpression(ElseExpression elseExpression) {
        this.elseExpression = elseExpression;
        return true;
    }

    public boolean addElseIf (ElseIfExpression expression) {
        this.elseIfExpressions.add(expression);
        return true;
    }

    public Object getConditionalExpression() {
        return conditionalExpression;
    }

    public ElseExpression getElseExpression() {
        return elseExpression;
    }

    public List<ElseIfExpression> getElseIfExpressions() {
        return elseIfExpressions;
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
            if (isTrue(context.resolve(conditionalExpression))) {
                return content.render(outputStream, context);
            } else {
                for (ElseIfExpression exp : elseIfExpressions) {
                    if (exp.render(outputStream, context))
                        return true;
                }
                if (elseExpression != null) {
                    return elseExpression.render(outputStream, context);
                }
                return true;
            }
        } catch (CalculateException e) {
            throw new RenderException(e);
        }
    }

    @Override
    public IfExpression compose(JtwigResource resource) throws ComposeException {
        this.content = content.compose(resource);
        for (int i = 0;i<this.elseIfExpressions.size();i++)
            elseIfExpressions.set(i, elseIfExpressions.get(i).compose(resource));

        if (elseExpression != null)
            elseExpression = elseExpression.compose(resource);

        return this;
    }

    @Override
    public boolean replace(BlockExpression expression) throws ComposeException {
        boolean replaced = this.content.replace(expression);

        for (int i = 0;i<this.elseIfExpressions.size();i++)
            replaced = replaced || elseIfExpressions.get(i).replace(expression);

        if (elseExpression != null)
            replaced =  replaced || elseExpression.replace(expression);

        return replaced;
    }

    public static class ElseIfExpression implements Renderable, Composable<ElseIfExpression> {
        private Object condition;
        private Content content;

        public ElseIfExpression(Object condition) {
            this.condition = condition;
        }

        public boolean setContent(Content content) {
            this.content = content;
            return true;
        }

        public Content getContent() {
            return content;
        }

        @Override
        public boolean render(OutputStream outputStream, JtwigContext context) throws RenderException {
            try {
                if (isTrue(context.resolve(condition))) {
                    return content.render(outputStream, context);
                }
                return false;
            } catch (CalculateException e) {
                throw new RenderException(e);
            }
        }

        @Override
        public ElseIfExpression compose(JtwigResource resource) throws ComposeException {
            content = content.compose(resource);
            return this;
        }

        @Override
        public boolean replace(BlockExpression expression) throws ComposeException {
            return content.replace(expression);
        }
    }

    public static class ElseExpression implements Renderable, Composable<ElseExpression> {
        private Content content;

        public ElseExpression(Content content) {
            this.content = content;
        }

        @Override
        public boolean render(OutputStream outputStream, JtwigContext context) throws RenderException {
            content.render(outputStream, context);
            return true;
        }

        @Override
        public ElseExpression compose(JtwigResource resource) throws ComposeException {
            content = content.compose(resource);
            return this;
        }

        @Override
        public boolean replace(BlockExpression expression) throws ComposeException {
            return content.replace(expression);
        }
    }
}
