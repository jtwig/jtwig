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

package com.lyncode.jtwig.tree.tags;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.parser.positioning.Position;
import com.lyncode.jtwig.tree.api.AbstractContent;
import com.lyncode.jtwig.tree.api.Content;
import com.lyncode.jtwig.tree.api.Expression;
import com.lyncode.jtwig.tree.expressions.Constant;
import com.lyncode.jtwig.tree.expressions.FunctionElement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class Filter extends AbstractContent {
    private Content content;
    private List<FunctionElement> expressions = Lists.newArrayList();

    public Filter(Position position, FunctionElement expression) {
        super(position);
        this.expressions.add(expression);
    }

    public Expression addExpression(FunctionElement expression) {
        this.expressions.add(expression);
        return expression;
    }

    public boolean setContent(Content content) {
        this.content = content;
        return true;
    }

    @Override
    public void render(OutputStream outputStream, JtwigContext context) throws RenderException {
        OutputStream contentStream = new ByteArrayOutputStream();
        this.content.render(contentStream, context);
        Expression constant = new Constant<>(contentStream.toString());

        this.expressions.get(0).addArgument(0, constant);

        try {
            outputStream.write(Iterables.getLast(this.expressions).calculate(context).toString().getBytes());
        } catch (IOException | CalculateException e) {
            throw new RenderException(e);
        }
    }

    @Override
    public String toString() {
        return "Render the result of "+content;
    }
}
