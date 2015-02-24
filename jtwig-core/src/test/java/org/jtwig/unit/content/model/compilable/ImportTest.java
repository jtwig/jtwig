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

package org.jtwig.unit.content.model.compilable;

import org.jtwig.unit.AbstractJtwigTest;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.compilable.Import;
import org.jtwig.exception.ParseException;
import org.jtwig.exception.RenderException;
import org.jtwig.expressions.model.Constant;
import org.jtwig.parser.model.JtwigPosition;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ImportTest extends AbstractJtwigTest {
    @Test(expected = RenderException.class)
    public void ensureGeneralParseExceptionDuringRenderThrowsRenderException() throws Exception {
        resource = classpathResource("templates/acceptance/import/import.twig");
        JtwigPosition pos = new JtwigPosition(resource, 1, 1);
        
        Import.General imp = new Import.General(pos, new Constant("imported.twig"), "imported");
        Renderable r = imp.compile(compileContext);
        when(renderContext.with(anyString(), any())).thenThrow(ParseException.class);
        r.render(renderContext);
    }
    @Test(expected = RenderException.class)
    public void ensureFromParseExceptionDuringRenderThrowsRenderException() throws Exception {
        resource = classpathResource("templates/acceptance/import/import.twig");
        JtwigPosition pos = new JtwigPosition(resource, 1, 1);
        
        Import.From imp = new Import.From(pos, new Constant("imported.twig"));
        imp.add("password", "password");
        Renderable r = imp.compile(compileContext);
        when(renderContext.with(anyString(), any())).thenThrow(ParseException.class);
        r.render(renderContext);
    }
    @Test
    public void importSelfWorks() throws Exception {
        resource = classpathResource("templates/acceptance/import/imported.twig");
        JtwigPosition pos = new JtwigPosition(resource, 1, 1);
        
        Import.General imp = new Import.General(pos, new Constant("_self"), "imported");
        Renderable r = imp.compile(compileContext);
        r.render(renderContext);
    }
}