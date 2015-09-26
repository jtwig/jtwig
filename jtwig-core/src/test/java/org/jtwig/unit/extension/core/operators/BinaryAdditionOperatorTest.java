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

import org.jtwig.extension.core.operators.BinaryAdditionOperator;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.unit.AbstractJtwigTest;
import static org.junit.Assert.*;
import org.junit.Test;

public class BinaryAdditionOperatorTest extends AbstractJtwigTest {
    BinaryAdditionOperator underTest = new BinaryAdditionOperator("+", 0);
    JtwigPosition position = new JtwigPosition(resource, 1, 1);
    
    @Test
    public void testAdditionWithNulls() throws Exception {
        assertEquals(1L, underTest.render(renderContext, position, 1, null));
        assertEquals(1L, underTest.render(renderContext, position, null, 1));
    }
    
    @Test
    public void testAdditionWithObjects() throws Exception {
        assertEquals(1L, underTest.render(renderContext, position, new Object(), null));
        assertEquals(1L, underTest.render(renderContext, position, new Object(), 0));
        assertEquals(2L, underTest.render(renderContext, position, new Object(), 1));
    }
    
    @Test
    public void testAdditionWithStrings() throws Exception {
        assertEquals(0L, underTest.render(renderContext, position, 0, "a"));
        assertEquals(0L, underTest.render(renderContext, position, 0, "test"));
        assertEquals(2L, underTest.render(renderContext, position, 0, "2test"));
        assertEquals(5L, underTest.render(renderContext, position, 0, "5"));
        assertEquals(0L, underTest.render(renderContext, position, 0, "true"));
    }
    
    @Test
    public void testAdditionWithBooleans() throws Exception {
        assertEquals(2L, underTest.render(renderContext, position, true, true));
        assertEquals(1L, underTest.render(renderContext, position, true, false));
        assertEquals(0L, underTest.render(renderContext, position, false, false));
    }
}