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
package org.jtwig.unit.extension.core.api.operator;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.jtwig.expressions.api.Expression;
import org.jtwig.extension.core.operators.UnaryNegativeOperator;
import org.jtwig.types.Undefined;
import org.jtwig.unit.AbstractJtwigTest;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class UnaryOperatorTest extends AbstractJtwigTest {
    @Test
    public void testWithUndefinedInput() throws Exception {
        Expression expr = mock(Expression.class);
        doReturn(Undefined.UNDEFINED).when(expr).calculate(renderContext);
        
        assertThat(new UnaryNegativeOperator("-", 0)
                .compile(env, null, compileContext, expr)
                .calculate(renderContext),
                is(equalTo((Object)0L)));
    }
}