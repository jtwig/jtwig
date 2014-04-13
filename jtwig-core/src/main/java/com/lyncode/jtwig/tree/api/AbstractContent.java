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

package com.lyncode.jtwig.tree.api;

import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.parser.positioning.Position;
import com.lyncode.jtwig.tree.structural.Block;
import com.lyncode.jtwig.unit.resource.JtwigResource;

public abstract class AbstractContent implements Content, Tag {
    private final TagInformation begin = new TagInformation();
    private final TagInformation end = new TagInformation();
    private final Position position;

    protected AbstractContent(Position position) {
        this.position = position;
    }

    public TagInformation begin() {
        return this.begin;
    }
    public TagInformation end() {
        return this.end;
    }
    public Position getPosition() {
        return position;
    }

    @Override
    public Content compile(JtwigParser parser, JtwigResource resource) throws CompileException {
        return this;
    }

    @Override
    public boolean replace(Block expression) throws CompileException {
        return false;
    }
}
