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

import com.lyncode.jtwig.addons.Addon;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.render.RenderContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Spaceless extends Addon {
    private static String removeSpaces(String input) {
        return input
                .replaceAll("\\s+<", "<")
                .replaceAll(">\\s+", ">");
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        return new Compiled(super.compile(context));
    }

    private static class Compiled implements Renderable {
        private final Renderable content;

        private Compiled(Renderable content) {
            this.content = content;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            content.render(context.newRenderContext(outputStream));
            String result = removeSpaces(outputStream.toString());
            try {
                context.write(result.getBytes());
            } catch (IOException e) {
                throw new RenderException(e);
            }
        }
    }
}
