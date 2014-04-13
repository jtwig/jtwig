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

package com.lyncode.jtwig.tree.documents;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.tree.api.Content;
import com.lyncode.jtwig.tree.helper.RenderStream;
import com.lyncode.jtwig.tree.structural.Block;
import com.lyncode.jtwig.tree.structural.Extends;
import com.lyncode.jtwig.unit.resource.JtwigResource;

import java.util.ArrayList;
import java.util.List;

public class JtwigExtendsDocument implements JtwigDocument {
    private Extends anExtends;
    private List<Block> blocks = new ArrayList<Block>();

    public JtwigExtendsDocument(Extends anExtends) {
        this.anExtends = anExtends;
    }

    public boolean add (Block block) {
        blocks.add(block);
        return true;
    }

    @Override
    public void render(RenderStream renderStream, JtwigContext context) throws RenderException {
    }

    @Override
    public Content compile(JtwigParser parser, JtwigResource resource) throws CompileException {
        try {
            for (int i = 0; i < blocks.size(); i++)
                blocks.set(i, blocks.get(i).compile(parser, resource));

            JtwigResource jtwigResource = resource.resolve(anExtends.getPath());

            JtwigParser nestedParser = parser.clone(jtwigResource);
            Content content = JtwigParser.parse(nestedParser, jtwigResource)
                    .compile(nestedParser, jtwigResource);
            for (Block expression : blocks) {
                content.replace(expression);
            }
            return content;
        } catch (ResourceException | ParseException e) {
            throw new CompileException(e);
        }
    }

    @Override
    public boolean replace(Block expression) throws CompileException {
        boolean replaced = false;
        for (Block container : blocks)
            replaced = replaced || container.replace(expression);

        return replaced;
    }
}
