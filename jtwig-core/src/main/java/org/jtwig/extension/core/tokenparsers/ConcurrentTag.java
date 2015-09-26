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

public class ConcurrentTag extends Tag {

    public ConcurrentTag(Loader.Resource resource, JtwigContentParser content, JtwigBasicParser basic, JtwigExpressionParser expr, JtwigTagPropertyParser tag) {
        super(resource, content, basic, expr, tag);
    }

    @Override
    public String getKeyword() {
        return "concurrent";
    }

    @Override
    public Compilable model(JtwigPosition pos) {
        return new Concurrent();
    }
    
    public static class Concurrent extends Content<Concurrent> {

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
                try {
                    context.renderConcurrent(content);
                } catch (IOException e) {
                    throw new RenderException(e);
                }
            }
        }
    }
}