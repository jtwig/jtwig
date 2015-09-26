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
import org.jtwig.extension.core.operators.BinarySubtractionOperator;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.unit.AbstractJtwigTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class BinarySubtractionOperatorTest extends AbstractJtwigTest {
    BinarySubtractionOperator underTest = new BinarySubtractionOperator("-", 0);
    JtwigPosition position = new JtwigPosition(resource, 1, 1);
    
    @Test
    public void generalTest() throws Exception {
        assertEquals(1L, underTest.render(renderContext, position, 2, 1));
        assertEquals(1L, underTest.render(renderContext, position, 2, "1"));
        assertEquals(1L, underTest.render(renderContext, position, "2", 1));
        assertEquals(1L, underTest.render(renderContext, position, "2", "1"));

        assertEquals(2L, underTest.render(renderContext, position, 2, null));
        assertEquals(2L, underTest.render(renderContext, position, "2", null));
        assertEquals(new BigDecimal("2.0"), underTest.render(renderContext, position, 2.0, null));
        assertEquals(1L, underTest.render(renderContext, position, new Object(), null));
        assertEquals(0L, underTest.render(renderContext, position, null, null));
        assertEquals(-2L, underTest.render(renderContext, position, null, 2));
        assertEquals(-2L, underTest.render(renderContext, position, null, "2"));
        assertEquals(new BigDecimal("-2.0"), underTest.render(renderContext, position, null, 2.0));
        assertEquals(-1L, underTest.render(renderContext, position, null, new Object()));

        assertEquals(0L, underTest.render(renderContext, position, true, 1));
        assertEquals(-1L, underTest.render(renderContext, position, true, 2));
        assertEquals(-1L, underTest.render(renderContext, position, true, "2"));
        assertEquals(new BigDecimal("-1.0"), underTest.render(renderContext, position, true, 2.0));
        assertEquals(1L, underTest.render(renderContext, position, true, null));
        assertEquals(0L, underTest.render(renderContext, position, true, new Object()));
        assertEquals(-1L, underTest.render(renderContext, position, false, 1));
        assertEquals(-2L, underTest.render(renderContext, position, false, 2));
    }
}