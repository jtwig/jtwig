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
import com.lyncode.jtwig.parser.positioning.Position;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.tree.api.AbstractContent;
import com.lyncode.jtwig.tree.api.Expression;
import com.lyncode.jtwig.tree.api.TagInformation;
import com.lyncode.jtwig.tree.structural.Block;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.lyncode.jtwig.util.BooleanOperations.isTrue;

public class IfExpression extends AbstractContent {
    private final List<Case> cases = new ArrayList<>();

    public IfExpression(Position position, Case aCase) {
        super(position);
        cases.add(aCase);
    }

    public IfExpression add (Case newCase) {
        this.cases.add(newCase);
        return this;
    }

    public Case current () {
        return cases.get(cases.size() - 1);
    }

    @Override
    public void render(OutputStream outputStream, JtwigContext context) throws RenderException {
        try {
            Iterator<Case> iterator = cases.iterator();
            while (iterator.hasNext()) {
                Case next = iterator.next();
                if (next.conditionIsTrue(context)) {
                    next.render(outputStream, context);
                    break;
                }
            }
        } catch (CalculateException e) {
            throw new RenderException(e);
        }
    }

    @Override
    public IfExpression compile(JtwigParser parser, JtwigResource resource) throws CompileException {
        for (int i = 0;i<cases.size();i++)
            cases.set(i, cases.get(i).compile(parser, resource));
        return this;
    }

    @Override
    public boolean replace(Block expression) throws CompileException {
        boolean replaced = false;
        for (Case aCase : cases) {
            replaced |= aCase.replace(expression);
        }
        return replaced;
    }

    @Override
    public TagInformation begin() {
        return cases.get(0).begin();
    }

    @Override
    public TagInformation end() {
        return cases.get(cases.size() - 1).end();
    }

    public static class Case extends AbstractContent {
        private Expression expression;
        private JtwigContent content;

        public Case(Position position, Expression condition) {
            super(position);
            this.expression = condition;
        }

        public Case setContent(JtwigContent content) {
            this.content = content;
            return this;
        }

        public boolean conditionIsTrue (JtwigContext context) throws CalculateException {
            return isTrue(expression.calculate(context));
        }

        @Override
        public void render(OutputStream outputStream, JtwigContext context) throws RenderException {
            content.render(outputStream, context);
        }

        @Override
        public Case compile(JtwigParser parser, JtwigResource resource) throws CompileException {
            content = content.compile(parser, resource, begin(), end());
            return this;
        }

        @Override
        public boolean replace(Block expression) throws CompileException {
            return content.replace(expression);
        }
    }
}
