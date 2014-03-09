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
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.tree.api.Content;
import com.lyncode.jtwig.tree.api.Tag;
import com.lyncode.jtwig.tree.api.TagInformation;
import com.lyncode.jtwig.tree.content.JtwigContent;
import com.lyncode.jtwig.tree.helper.RenderStream;

import java.io.OutputStream;

public class Concurrent implements Content, Tag {

    private JtwigContent content;
    private TagInformation begin = new TagInformation();
    private TagInformation end = new TagInformation();

    public Concurrent() {
    }

    public Content getContent() {
        return content;
    }

    public boolean setContent(JtwigContent content) {
        this.content = content;
        return true;
    }

    @Override
    public String toString() {
        return "Concurrent";
    }

    @Override
    public boolean render(RenderStream renderStream, JtwigContext context) throws RenderException {
        return true;
    }

    @Override
    public Concurrent compile(JtwigParser parser, JtwigResource resource) throws CompileException {
        return this;
    }

    @Override
    public boolean replace(Block expression) throws CompileException {
        return false;
    }

    @Override
    public TagInformation begin() {
        return begin;
    }

    @Override
    public TagInformation end() {
        return end;
    }
}
