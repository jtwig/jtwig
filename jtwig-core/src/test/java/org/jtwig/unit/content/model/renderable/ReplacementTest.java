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

package org.jtwig.unit.content.model.renderable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.jtwig.Environment;
import org.jtwig.JtwigModelMap;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.renderable.Replacement;
import org.jtwig.exception.RenderException;
import org.jtwig.render.RenderContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReplacementTest {
    Environment env = new Environment();

    @Test
    public void replacementRendersOverridingContent() throws Exception {
        Renderable replacement = new Renderable() {
            @Override
            public void render(final RenderContext context) throws RenderException {
                try {
                    context.write("hello, new world!".getBytes());
                } catch (IOException ex) {
                    throw new RenderException(ex);
                }
            }
        };
        Replacement repl = new Replacement(replacement, Renderable.NOOP);
        
        OutputStream os = new ByteArrayOutputStream();
        RenderContext ctx = RenderContext.create(env, new JtwigModelMap(), os);
        repl.render(ctx);
        assertEquals("hello, new world!", os.toString());
    }
    
}