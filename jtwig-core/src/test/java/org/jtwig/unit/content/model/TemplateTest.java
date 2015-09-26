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

package org.jtwig.unit.content.model;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.jtwig.Environment;
import org.jtwig.JtwigModelMap;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Compilable;
import org.jtwig.content.model.Template;
import org.jtwig.extension.core.tokenparsers.model.Block;
import org.jtwig.content.model.compilable.Text;
import org.jtwig.render.RenderContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class TemplateTest {
    @Test
    public void ensureContentIsReturned() throws Exception {
        Compilable compilable = new  Text("hello, world");
        
        Template template = new Template(null);
        template.add(compilable);
        assertEquals("hello, world", render(template));
    }
    @Test
    public void ensureBlockTrackingWorksProperly() throws Exception {
        Block block = new Block(null, "test1");
        
        Template template = new Template(null);
        assertNull(template.block("test1"));
        template.track(block);
        assertEquals(block, template.block("test1"));
        assertEquals(block, template.blocks().get("test1"));
    }
    
    protected String render(final Compilable compilable) throws Exception {
        try (OutputStream os = new ByteArrayOutputStream()) {
            Environment env = new Environment();
            CompileContext compileCtx = new CompileContext(null, env);
            RenderContext renderCtx = RenderContext.create(env, new JtwigModelMap(), os);

            compilable.compile(compileCtx).render(renderCtx);
            return os.toString();
        }
    }
}