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

package com.lyncode.jtwig.tree.content;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.tree.api.Content;
import com.lyncode.jtwig.tree.api.Expression;
import com.lyncode.jtwig.tree.api.Tag;
import com.lyncode.jtwig.tree.api.TagInformation;
import com.lyncode.jtwig.tree.helper.RenderStream;
import com.lyncode.jtwig.tree.structural.Block;

import java.util.ArrayList;
import java.util.List;

import static com.lyncode.jtwig.util.BooleanOperations.isTrue;

public class IfExpression implements Content, Tag {
    private Expression conditionalExpression;
    private JtwigContent content;
    private ElseExpression elseExpression = null;
    private List<ElseIfExpression> elseIfExpressions = new ArrayList<ElseIfExpression>();

    private TagInformation begin = new TagInformation();
    private TagInformation end = new TagInformation();

    public TagInformation begin() {
        return begin;
    }

    public TagInformation end() {
        return end;
    }

    public IfExpression(Expression conditionalExpression) {
        this.conditionalExpression = conditionalExpression;
    }

    public boolean setElseExpression(ElseExpression elseExpression) {
        this.elseExpression = elseExpression;
        return true;
    }

    public boolean addElseIf(ElseIfExpression expression) {
        this.elseIfExpressions.add(expression);
        return true;
    }

    public boolean setContent(JtwigContent content) {
        this.content = content;
        return true;
    }

    @Override
    public boolean render(RenderStream renderStream, JtwigContext context) throws RenderException {
        try {
            if (isTrue(conditionalExpression.calculate(context))) {
                return content.render(renderStream, context);
            } else {
                for (ElseIfExpression exp : elseIfExpressions) {
                    if (exp.render(renderStream, context)) {
                        return true;
                    }
                }
                if (hasElse()) {
                    return elseExpression.render(renderStream, context);
                }
                return true;
            }
        } catch (CalculateException e) {
            throw new RenderException(e);
        }
    }

    @Override
    public IfExpression compile(JtwigParser parser, JtwigResource resource) throws CompileException {
        TagInformation end = end();
        if (!elseIfExpressions.isEmpty()) {
            end = elseIfExpressions.get(0).tag();
        } else if (hasElse()) {
            end = elseExpression.tag();
        }

        this.content = content.compile(parser, resource, begin(), end);

        int size = this.elseIfExpressions.size();
        for (int i = 0; i < size; i++) {
            end = end();
            if (i < size - 1) {
                end = elseIfExpressions.get(i + 1).tag();
            } else if (hasElse()) {
                end = elseExpression.tag();
            }
            ElseIfExpression elseIfExpression = elseIfExpressions.get(i);
            elseIfExpressions.set(i, elseIfExpression.compile(parser, resource, elseIfExpression.tag(), end));
        }

        if (hasElse()) {
            elseExpression = elseExpression.compile(parser, resource, elseExpression.tag(), end());
        }

        return this;
    }

    private boolean hasElse() {
        return elseExpression != null;
    }

    @Override
    public boolean replace(Block expression) throws CompileException {
        boolean replaced = this.content.replace(expression);

        for (int i = 0; i < this.elseIfExpressions.size(); i++) {
            replaced = replaced || elseIfExpressions.get(i).replace(expression);
        }

        if (hasElse()) {
            replaced = replaced || elseExpression.replace(expression);
        }

        return replaced;
    }

    public static class ElseIfExpression implements Content {
        private Expression condition;
        private JtwigContent content;
        private TagInformation tag = new TagInformation();

        public ElseIfExpression(Expression condition) {
            this.condition = condition;
        }

        public boolean setContent(JtwigContent abstractContent) {
            this.content = abstractContent;
            return true;
        }

        public Content getContent() {
            return content;
        }

        @Override
        public boolean render(RenderStream renderStream, JtwigContext context) throws RenderException {
            try {
                if (isTrue(condition.calculate(context))) {
                    return content.render(renderStream, context);
                }
                return false;
            } catch (CalculateException e) {
                throw new RenderException(e);
            }
        }

        @Override
        public ElseIfExpression compile(JtwigParser parser, JtwigResource resource) throws CompileException {
            content = content.compile(parser, resource);
            return this;
        }

        public ElseIfExpression compile(JtwigParser parser, JtwigResource resource, TagInformation begin, TagInformation end) throws CompileException {
            content = content.compile(parser, resource, begin, end);
            return this;
        }

        @Override
        public boolean replace(Block expression) throws CompileException {
            return content.replace(expression);
        }

        public TagInformation tag() {
            return this.tag;
        }
    }

    public static class ElseExpression implements Content {
        private JtwigContent content;
        private TagInformation tag = new TagInformation();

        public boolean setContent(JtwigContent content) {
            this.content = content;
            return true;
        }

        @Override
        public boolean render(RenderStream renderStream, JtwigContext context) throws RenderException {
            content.render(renderStream, context);
            return true;
        }

        @Override
        public ElseExpression compile(JtwigParser parser, JtwigResource resource) throws CompileException {
            content = content.compile(parser, resource);
            return this;
        }

        public ElseExpression compile(JtwigParser parser, JtwigResource resource, TagInformation begin, TagInformation end) throws CompileException {
            content = content.compile(parser, resource, begin, end);
            return this;
        }

        @Override
        public boolean replace(Block expression) throws CompileException {
            return content.replace(expression);
        }

        public TagInformation tag() {
            return this.tag;
        }
    }
}
