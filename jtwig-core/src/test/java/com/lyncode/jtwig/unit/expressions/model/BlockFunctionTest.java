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

package com.lyncode.jtwig.unit.expressions.model;

import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.expressions.model.BlockFunction;
import com.lyncode.jtwig.expressions.model.Constant;
import com.lyncode.jtwig.expressions.model.FunctionElement;
import com.lyncode.jtwig.functions.parameters.input.InputParameters;
import com.lyncode.jtwig.render.RenderContext;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BlockFunctionTest {
    
    @Test
    public void complainsOnLackOfArgument() throws Exception {
        BlockFunction block = new BlockFunction(null);
        CompileContext compilectx = mock(CompileContext.class);
        try {
            block.compile(compilectx);
            Assert.fail("Should have complained about lack of argument");
        } catch (CompileException e) {
            Assert.assertEquals("Block function requires a single argument", e.getMessage());
        }
    }

    @Test
    public void expressionCalculationQueriesContext() throws Exception {
        RenderContext context = mock(RenderContext.class);
        CompileContext compilectx = mock(CompileContext.class);
        BlockFunction block = new BlockFunction(null);
        block.add(new Constant<>("title"));
        block.compile(compilectx)
                .calculate(context);

        verify(compilectx).replacement(eq("title"));
    }
}