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

import java.math.BigDecimal;
import org.jtwig.extension.core.operators.BinaryMultiplicationOperator;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.unit.AbstractJtwigTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryMultiplicationOperatorTest extends AbstractJtwigTest {
    BinaryMultiplicationOperator underTest = new BinaryMultiplicationOperator("*", 0);
    JtwigPosition position = new JtwigPosition(resource, 1, 1);
    
    @Test
    public void generalTest() throws Exception {
        assertEquals(new BigDecimal("7.5"), underTest.render(renderContext, position, 5, 1.5));
        assertEquals(new BigDecimal("7.5"), underTest.render(renderContext, position, 5, "1.5"));
        assertEquals(new BigDecimal("7.5"), underTest.render(renderContext, position, "5", 1.5));
        assertEquals(new BigDecimal("7.5"), underTest.render(renderContext, position, "5", "1.5"));
        
        assertEquals(0L, underTest.render(renderContext, position, null, "1.5"));
        assertEquals(0L, underTest.render(renderContext, position, "5", null));
        
        assertEquals(new BigDecimal("1.5"), underTest.render(renderContext, position, new Object(), "1.5"));
        assertEquals(5L, underTest.render(renderContext, position, 5, new Object()));
    }
}