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

package org.jtwig.unit.extension.core.operators;

import org.jtwig.exception.CompileException;
import org.jtwig.expressions.api.Expression;
import org.jtwig.expressions.model.Constant;
import org.jtwig.extension.core.operators.BinaryCompositionOperator;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.unit.AbstractJtwigTest;
import org.junit.Test;

public class BinaryCompositionOperatorTest extends AbstractJtwigTest {
    BinaryCompositionOperator underTest = new BinaryCompositionOperator("|", 0);
    JtwigPosition position = new JtwigPosition(resource, 1, 1);
    
    /**
     * Compiling a composition operator with a non-filter argument shouldn't
     * happen during normal operation.
     * @throws Exception 
     */
    @Test(expected = CompileException.class)
    public void testCompileWithNonFilterArgument() throws Exception {
        underTest.compile(env, position, compileContext, Expression.NOOP, Expression.NOOP);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRender() throws Exception {
        underTest.render(renderContext, position, env, this);
    }
}