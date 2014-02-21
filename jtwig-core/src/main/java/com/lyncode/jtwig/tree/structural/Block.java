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

package com.lyncode.jtwig.tree.structural;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.tree.api.Content;

import java.io.OutputStream;

public class Block implements Content {
    private String name;

    private Content content;

    public Block(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Content getContent() {
        return content;
    }

    public boolean setContent(Content content) {
        this.content = content;
        return true;
    }

    @Override
    public String toString() {
        return "Block "+getName();
    }

    @Override
    public boolean render(OutputStream outputStream, JtwigContext context) throws RenderException {
        return content.render(outputStream, context);
    }

    @Override
    public Block compile(JtwigResource resource) throws CompileException {
        this.content = content.compile(resource);
        return this;
    }

    @Override
    public boolean replace(Block expression) throws CompileException {
        return content.replace(expression);
    }
}
