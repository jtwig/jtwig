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

package com.lyncode.jtwig.content.model.compilable;

import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Compilable;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.resource.JtwigResource;

import java.util.ArrayList;
import java.util.List;

public class Extends extends AbstractElement {
    private final String relativePath;
    private List<Block> blocks = new ArrayList<>();

    public Extends(String relativeUrl) {
        this.relativePath = relativeUrl;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        try {
            JtwigResource extendResource = context.retrieve(relativePath);
            CompileContext newContext = context.clone().withResource(extendResource);

            for (Block block : blocks)
                newContext.withReplacement(block.name(), block.compile(context));

            Compilable parsed = newContext.parse(extendResource);
            return parsed.compile(newContext);
        } catch (ResourceException | ParseException e) {
            throw new CompileException(e);
        }
    }

    public Extends add(Block block) {
        this.blocks.add(block);
        return this;
    }
}
