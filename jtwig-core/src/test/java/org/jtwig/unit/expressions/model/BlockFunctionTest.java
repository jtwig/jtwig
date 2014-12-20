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

import org.jtwig.content.model.BasicTemplate;
import org.jtwig.content.model.Template;
import org.jtwig.exception.CompileException;
import org.jtwig.expressions.model.BlockFunction;
import org.jtwig.expressions.model.Constant;
import org.jtwig.unit.AbstractUnitTest;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class BlockFunctionTest extends AbstractUnitTest {
    
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
        when(renderContext.getRenderingTemplate()).thenReturn(template);
        BlockFunction block = new BlockFunction(null);
        block.add(new Constant<>("title"));
        block.compile(compileContext)
                .calculate(renderContext);

        verify(template).block(eq("title"));
    }
}
