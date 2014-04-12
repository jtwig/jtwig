/**
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
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.tree.api.Content;
import com.lyncode.jtwig.tree.helper.RenderStream;
import com.lyncode.jtwig.tree.structural.Block;

import java.io.IOException;

public class JtwigRootContent implements Content {

    private Content content;

    public JtwigRootContent(Content content) {
        this.content = content;
    }

    @Override
    public boolean render(RenderStream renderStream, JtwigContext context) throws RenderException {
        content.render(renderStream, context);
        renderStream.waitForExecutorCompletion();
        try {
            renderStream.close();
            renderStream.merge();
        } catch (IOException e) {
            throw new RenderException(e);
        }
        return true;
    }

    @Override
    public Content compile(JtwigParser parser, JtwigResource resource) throws CompileException {
        content = content.compile(parser, resource);
        return this;
    }

    @Override
    public boolean replace(Block expression) throws CompileException {
        return content.replace(expression);
    }
}
