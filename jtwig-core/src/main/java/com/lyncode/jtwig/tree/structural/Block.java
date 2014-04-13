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

package com.lyncode.jtwig.tree.structural;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.parser.positioning.Position;
import com.lyncode.jtwig.tree.api.AbstractContent;
import com.lyncode.jtwig.tree.api.Content;
import com.lyncode.jtwig.tree.content.JtwigContent;
import com.lyncode.jtwig.tree.helper.RenderStream;
import com.lyncode.jtwig.unit.resource.JtwigResource;

public class Block extends AbstractContent {
    private String name;
    private JtwigContent content;

    public Block(Position position, String name) {
        super(position);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Content getContent() {
        return content;
    }

    public Block setContent(JtwigContent content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return "Block "+getName();
    }

    @Override
    public void render(RenderStream renderStream, JtwigContext context) throws RenderException {
        content.render(renderStream, context);
    }

    @Override
    public Block compile(JtwigParser parser, JtwigResource resource) throws CompileException {
        this.content = content.compile(parser, resource, begin(), end());
        return this;
    }

    @Override
    public boolean replace(Block expression) throws CompileException {
        return content.replace(expression);
    }
}
