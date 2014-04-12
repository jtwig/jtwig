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

package com.lyncode.jtwig.addons.spaceless;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.parser.addons.JtwigContentAddon;
import com.lyncode.jtwig.tree.helper.RenderStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Spaceless extends JtwigContentAddon {

    @Override
    public void render(RenderStream renderStream, JtwigContext context) throws RenderException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        RenderStream local = new RenderStream(output);
        getContent().render(local, context);
        String result = removeSpaces(output.toString());
        try {
            renderStream.write(result.getBytes());
        } catch (IOException e) {
            throw new RenderException(e);
        }
    }

    private String removeSpaces(String input) {
        return input
                .replaceAll("\\s+<", "<")
                .replaceAll(">\\s+", ">");
    }
}
