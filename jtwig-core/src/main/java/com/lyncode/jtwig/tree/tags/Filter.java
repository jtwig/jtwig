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
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.tree.api.Content;
import com.lyncode.jtwig.tree.api.Expression;
import com.lyncode.jtwig.tree.api.Tag;
import com.lyncode.jtwig.tree.api.TagInformation;
import com.lyncode.jtwig.tree.content.JtwigRootContent;
import com.lyncode.jtwig.tree.expressions.Constant;
import com.lyncode.jtwig.tree.expressions.FunctionElement;
import com.lyncode.jtwig.tree.helper.RenderStream;
import com.lyncode.jtwig.tree.structural.Block;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class Filter implements Content, Tag {

    private Content content;
    private TagInformation begin = new TagInformation();
    private TagInformation end = new TagInformation();
    private List<FunctionElement> expressions = Lists.newArrayList();

    public Filter() {
    }

    public Filter(FunctionElement expression) {
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
    public boolean render(RenderStream renderStream, JtwigContext context) throws RenderException {
        RenderStream renderStream1 = new RenderStream(new ByteArrayOutputStream());
        content.render(renderStream1, context);
        Expression constant = new Constant<>(renderStream1.getOuputStream().toString());

        this.expressions.get(0).getArguments().set(0, constant);

        try {
            renderStream.write(Iterables.getLast(this.expressions).calculate(context).toString().getBytes());
            return true;
        } catch (IOException | CalculateException e) {
            throw new RenderException(e);
        }
    }

    @Override
    public Content compile(JtwigParser parser, JtwigResource resource) throws CompileException {
        return new JtwigRootContent(this);
    }

    @Override
    public boolean replace(Block expression) throws CompileException {
        return false;
    }

    @Override
    public String toString() {
        return "Render the result of " + content;
    }

    @Override
    public TagInformation begin() {
        return this.begin;
    }

    @Override
    public TagInformation end() {
        return this.end;
    }
}
