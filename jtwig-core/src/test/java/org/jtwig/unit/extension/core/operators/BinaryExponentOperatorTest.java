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
import java.math.MathContext;
import java.math.RoundingMode;
import org.jtwig.extension.core.operators.BinaryExponentOperator;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.unit.AbstractJtwigTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryExponentOperatorTest extends AbstractJtwigTest {
    BinaryExponentOperator underTest = new BinaryExponentOperator("**", 0);
    JtwigPosition position = new JtwigPosition(resource, 1, 1);
    
    @Test
    public void generalTest() throws Exception {
        assertEquals(8L, underTest.render(renderContext, position, 2, 3));
        assertEquals(8L, underTest.render(renderContext, position, "2", 3));
        // Rounding necessary due to floating-point arithmetic. Any better solutions? - Thomas Wilson
        assertEquals(new BigDecimal("9.261"), ((BigDecimal)underTest.render(renderContext, position, 2.1D, 3)).round(new MathContext(4, RoundingMode.HALF_UP)));
        assertEquals(new BigDecimal("9.261"), ((BigDecimal)underTest.render(renderContext, position, 2.1F, "3")).round(new MathContext(4, RoundingMode.HALF_UP)));
        assertEquals(0L, underTest.render(renderContext, position, null, "3"));
        assertEquals(1L, underTest.render(renderContext, position, 2, null));
        assertEquals(2L, underTest.render(renderContext, position, 2, new Object()));
    }
}