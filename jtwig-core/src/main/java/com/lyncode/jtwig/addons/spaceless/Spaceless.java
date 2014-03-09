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

package com.lyncode.jtwig.addons.spaceless;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.parser.addons.JtwigContentAddon;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Spaceless extends JtwigContentAddon {

    @Override
    public boolean render(OutputStream outputStream, JtwigContext context) throws RenderException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        getContent().render(output, context);
        String result = removeSpaces(output.toString());
        try {
            outputStream.write(result.getBytes());
        } catch (IOException e) {
            throw new RenderException(e);
        }
        return true;
    }

    private String removeSpaces(String input) {
        return input
                .replaceAll("\\s+<", "<")
                .replaceAll(">\\s+", ">");
    }
}
