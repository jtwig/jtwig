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
import com.lyncode.jtwig.content.api.Tag;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;

import java.util.ArrayList;
import java.util.List;

public class Extends extends AbstractElement {
    private final String relativePath;
    private List<Block> blocks = new ArrayList<>();
    private List<Compilable> tags = new ArrayList<>();

    public Extends(String relativeUrl) {
        this.relativePath = relativeUrl;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        Compiled compiled = new Compiled();
        for(Compilable tag : tags) {
            compiled.addTag(tag.compile(context));
        }
        
        // Build the template we're going to extend
        try {
            JtwigResource extendResource = context.retrieve(relativePath);
            context = context.clone().withResource(extendResource);
            
            for (Block block : blocks)
                context.withReplacement(block.name(), block.compile(context));

            Compilable parsed = context.parse(extendResource);
            compiled.setContent(parsed.compile(context));
            return compiled;
        } catch (ResourceException | ParseException e) {
            throw new CompileException(e);
        }
    }

    public Extends add(Block block) {
        this.blocks.add(block);
        return this;
    }
    
    public Extends addTag(Compilable tag) {
        this.tags.add(tag);
        return this;
    }
    
    private static class Compiled implements Renderable {
        private Renderable content;
        private List<Renderable> tags = new ArrayList<>();

        @Override
        public void render(RenderContext context) throws RenderException {
            for(Renderable tag : tags)
                tag.render(context);
            
            content.render(context);
        }
        
        public Compiled addTag(Renderable tag) {
            tags.add(tag);
            return this;
        }
        
        public Compiled setContent(Renderable content) {
            this.content = content;
            return this;
        }
        
    }
}
