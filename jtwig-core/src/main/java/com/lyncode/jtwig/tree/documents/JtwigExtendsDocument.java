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

package com.lyncode.jtwig.tree.documents;

import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.tree.content.Content;
import com.lyncode.jtwig.tree.structural.BlockExpression;
import com.lyncode.jtwig.tree.structural.ExtendsExpression;

import java.util.ArrayList;
import java.util.List;

public class JtwigExtendsDocument implements JtwigDocument {
    private ExtendsExpression extendsExpression;
    private List<BlockExpression> blocks = new ArrayList<BlockExpression>();

    public JtwigExtendsDocument(ExtendsExpression extendsExpression) {
        this.extendsExpression = extendsExpression;
    }

    public boolean add (BlockExpression blockExpression) {
        blocks.add(blockExpression);
        return true;
    }

    @Override
    public Content compile(JtwigResource resource) throws CompileException {
        try {
            for (int i = 0; i < blocks.size(); i++)
                blocks.set(i, blocks.get(i).compile(resource));

            JtwigResource jtwigResource = resource.resolve(extendsExpression.getPath());

            Content content = JtwigParser.parse(jtwigResource).compile(jtwigResource);
            for (BlockExpression expression : blocks) {
                content.replace(expression);
            }

            return content;
        } catch (ResourceException e) {
            throw new CompileException(e);
        } catch (ParseException e) {
            throw new CompileException(e);
        }
    }

    @Override
    public boolean replace(BlockExpression expression) throws CompileException {
        boolean replaced = false;
        for (BlockExpression container : blocks)
            replaced = replaced || container.replace(expression);

        return replaced;
    }
}
