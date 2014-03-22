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

package com.lyncode.jtwig.parser.addons;

import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.tree.api.Content;
import com.lyncode.jtwig.tree.api.Tag;
import com.lyncode.jtwig.tree.api.TagInformation;
import com.lyncode.jtwig.tree.content.JtwigContent;
import com.lyncode.jtwig.tree.structural.Block;

public abstract class JtwigContentAddon implements Content, Tag {
    private JtwigContent content;
    private TagInformation begin = new TagInformation();
    private TagInformation end = new TagInformation();


    public JtwigContent getContent() {
        return content;
    }

    public boolean setContent(JtwigContent content) {
        this.content = content;
        return true;
    }

    @Override
    public TagInformation begin() {
        return begin;
    }

    @Override
    public TagInformation end() {
        return end;
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
