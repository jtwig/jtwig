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

package org.jtwig.unit.extension.core.functions;

import org.jtwig.content.model.Template;
import org.jtwig.extension.core.tokenparsers.model.Block;
import org.jtwig.exception.RenderException;
import org.jtwig.extension.core.functions.BlockFunction;
import org.jtwig.render.RenderContext;
import org.jtwig.unit.AbstractJtwigTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BlockFunctionTest extends AbstractJtwigTest {
//    @Rule
//    public ExpectedException expectedException = ExpectedException.none();
//    
//    @Test
//    public void complainsOnLackOfArgument() throws Exception {
//        expectedException.expectMessage("The block function requires a block name");
//        
//        BlockFunction block = new BlockFunction();
//        block.evaluate(null, null);
//    }
//
//    @Test
//    public void expressionCalculationQueriesContext() throws Exception {
//        Template.Compiled template = mock(Template.Compiled.class);
//        when(template.getPrimordial()).thenReturn(template);
//        doReturn(template).when(renderContext).getRenderingTemplate();
//        BlockFunction block = new BlockFunction();
//        block.evaluate(env, renderContext, "title");
//
//        verify(template).block(eq("title"));
//    }
//    
//    @Test
//    public void calculateCapturesRenderException() throws Exception {
//        expectedException.expectMessage("Unable to render the block.");
//        
//        Block.CompiledBlock block = mock(Block.CompiledBlock.class);
//        doThrow(RenderException.class).when(block).render(any(RenderContext.class));
//        Template.Compiled template = mock(Template.Compiled.class);
//        when(template.block(anyString())).thenReturn(block);
//        when(template.getPrimordial()).thenReturn(template);
//        doReturn(template).when(renderContext).getRenderingTemplate();
//        
//        BlockFunction function = new BlockFunction();
//        function.evaluate(env, renderContext, "title");
//    }
}
