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

package com.lyncode.jtwig.tree.content;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.tree.api.Content;
import com.lyncode.jtwig.tree.api.Tag;
import com.lyncode.jtwig.tree.api.TagInformation;
import com.lyncode.jtwig.tree.helper.RenderStream;
import com.lyncode.jtwig.tree.structural.Block;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.lyncode.jtwig.tree.api.TagProperty.Trim;

public class JtwigContent implements Content {
    private List<Content> contents = new ArrayList<>();

    @Override
    public boolean render(RenderStream renderStream, JtwigContext context) throws RenderException {
        for (Content content : contents) {
            content.render(renderStream, context);
        }
//        try {
//            renderStream.merge();
//        } catch (IOException e) {
//            throw new RenderException(e);
//        }
        return true;
    }

    @Override
    public JtwigContent compile(JtwigParser parser, JtwigResource resource) throws CompileException {
        return compile(parser, resource, new TagInformation(), new TagInformation());
    }

    public JtwigContent compile(JtwigParser parser, JtwigResource resource, TagInformation begin, TagInformation end) throws CompileException {
        for (int i = 0; i < contents.size(); i++) {
            Content content = contents.get(i);
            if (content instanceof Text) {
                Text text = (Text) content;
                if (mustTrimLeft(i, begin)) {
                    text.trimLeft();
                }

                if (mustTrimRight(i, end)) {
                    text.trimRight();
                }
            }
            contents.set(i, content.compile(parser, resource));
        }
        return this;
    }

    private boolean mustTrimLeft(int position, TagInformation value) {
        if (value.hasRight(Trim)) {
            return true;
        }
        if (position <= 0) {
            return false;
        }
        Content before = contents.get(position - 1);
        if (!(before instanceof Tag)) {
            return false;
        }

        Tag tag = (Tag) before;
        return tag.end().hasRight(Trim);
    }

    private boolean mustTrimRight(int position, TagInformation value) {
        if (value.hasLeft(Trim)) {
            return true;
        }
        if (position >= contents.size() - 1) {
            return false;
        }
        Content after = contents.get(position + 1);
        if (!(after instanceof Tag)) {
            return false;
        }

        Tag tag = (Tag) after;
        return tag.begin().hasLeft(Trim);
    }

    @Override
    public boolean replace(Block expression) throws CompileException {
        boolean replaced = false;
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i) instanceof Block) {
                Block tmp = (Block) contents.get(i);
                if (expression.getName().equals(tmp.getName())) {
                    contents.set(i, expression.getContent());
                    replaced = true;
                }
            } else {
                replaced = replaced || contents.get(i).replace(expression);
            }
        }
        return replaced;
    }

    public JtwigContent add(Content content) {
        contents.add(content);
        return this;
    }

    protected List<Content> getContents() {
        return contents;
    }
}
