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

package org.jtwig.unit.expressions.model;

import org.jtwig.AbstractJtwigTest;
import org.jtwig.content.model.BasicTemplate;
import org.jtwig.content.model.Template;
import org.jtwig.exception.CompileException;
import org.jtwig.expressions.model.BlockFunction;
import org.jtwig.expressions.model.Constant;
import org.jtwig.MultiresourceUnitTest;
import org.jtwig.content.model.compilable.Block;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.RenderException;
import org.jtwig.render.RenderContext;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class BlockFunctionTest extends AbstractJtwigTest {
    
    @Test
    public void complainsOnLackOfArgument() throws Exception {
        BlockFunction block = new BlockFunction(null);
        try {
            block.compile(compileContext);
            Assert.fail("Should have complained about lack of argument");
        } catch (CompileException e) {
            Assert.assertEquals("Block function requires a single argument", e.getMessage());
        }
    }

    @Test
    public void expressionCalculationQueriesContext() throws Exception {
        Template.CompiledTemplate template = mock(BasicTemplate.CompiledBasicTemplate.class);
        when(template.getPrimordial()).thenReturn(template);
        doReturn(template).when(renderContext).getRenderingTemplate();
        BlockFunction block = new BlockFunction(null);
        block.add(new Constant<>("title"));
        block.compile(compileContext)
                .calculate(renderContext);

        verify(template).block(eq("title"));
    }
    
    @Test(expected = CalculateException.class)
    public void calculateCapturesRenderException() throws Exception {
        Block.CompiledBlock block = mock(Block.CompiledBlock.class);
        doThrow(RenderException.class).when(block).render(any(RenderContext.class));
        Template.CompiledTemplate template = mock(BasicTemplate.CompiledBasicTemplate.class);
        when(template.block(anyString())).thenReturn(block);
        when(template.getPrimordial()).thenReturn(template);
        doReturn(template).when(renderContext).getRenderingTemplate();
        
        BlockFunction function = new BlockFunction(null);
        function.add(new Constant<>("title"));
        function.compile(compileContext)
                .calculate(renderContext);
    }
}
