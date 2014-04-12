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

public class Text implements Content {
    private StringBuilder builder = new StringBuilder();
    private boolean trimLeft = false;
    private boolean trimRight = false;

    public Text() {
    }

    public Text(String result) {
        builder.append(result);
    }

    public boolean append(String piece) {
        builder.append(piece);
        return true;
    }

    public String getText() {
        return builder.toString();
    }

    @Override
    public void render(RenderStream renderStream, JtwigContext context) throws RenderException {
        try {
            renderStream.write(builder.toString().getBytes());
        } catch (IOException e) {
            throw new RenderException(e);
        }
    }

    @Override
    public Content compile(JtwigParser parser, JtwigResource resource) throws CompileException {
        String result = getText();
        if (trimLeft) {
            result = result.replaceAll("^\\s+", "");
        }
        if (trimRight) {
            result = result.replaceAll("\\s+$", "");
        }
        return new Text(result);
    }

    @Override
    public boolean replace(Block expression) throws CompileException {
        return false;
    }

    public String toString() {
        return "Text: " + builder.toString();
    }

    public void trimLeft() {
        this.trimLeft = true;
    }

    public void trimRight() {
        this.trimRight = true;
    }
}
