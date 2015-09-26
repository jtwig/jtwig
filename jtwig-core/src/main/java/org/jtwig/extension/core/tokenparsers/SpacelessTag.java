/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.extension.core.tokenparsers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Compilable;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.compilable.Content;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.extension.api.tokenparser.Tag;
import org.jtwig.loader.Loader;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.jtwig.render.RenderContext;

public class SpacelessTag extends Tag {

    public SpacelessTag(final Loader.Resource resource,
            final JtwigContentParser content,
            final JtwigBasicParser basic,
            final JtwigExpressionParser expr,
            final JtwigTagPropertyParser tag) {
        super(resource, content, basic, expr, tag);
    }

    @Override
    public String getKeyword() {
        return "spaceless";
    }

    @Override
    public Compilable model(final JtwigPosition pos) {
        return new Spaceless();
    }
    
    public static class Spaceless extends Content<Spaceless> {
        public Spaceless() {}

        @Override
        public Renderable compile(final CompileContext context) throws CompileException {
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
                String result = outputStream.toString()
                        .replaceAll("\\s+<", "<")
                        .replaceAll(">\\s+", ">");
                try {
                    context.write(result.getBytes());
                } catch (IOException e) {
                    throw new RenderException(e);
                }
            }
        }
    }
    
}