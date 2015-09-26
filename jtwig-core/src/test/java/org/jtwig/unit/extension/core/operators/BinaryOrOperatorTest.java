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

import org.jtwig.extension.core.operators.BinaryOrOperator;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.unit.AbstractJtwigTest;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class BinaryOrOperatorTest extends AbstractJtwigTest {
    BinaryOrOperator underTest = new BinaryOrOperator("or", 0);
    JtwigPosition position = new JtwigPosition(resource, 1, 1);
    
    @Test
    public void testComparisonOfObjects() throws Exception {
        assertTrue(underTest.render(renderContext, position, null, new Object()));
        assertFalse(underTest.render(renderContext, position, null, null));
        assertTrue(underTest.render(renderContext, position, new Object(), true));
        assertTrue(underTest.render(renderContext, position, new Object(), false));
        assertTrue(underTest.render(renderContext, position, new Object(), 0));
        assertTrue(underTest.render(renderContext, position, new Object(), 1));
        assertTrue(underTest.render(renderContext, position, new Object(), 5));
    }
    
    @Test
    public void testComparisonWithBooleans() throws Exception {
        assertTrue(underTest.render(renderContext, position, null, true));
        assertFalse(underTest.render(renderContext, position, null, false));
    }
    
    @Test
    public void testComparisonWithNumbers() throws Exception {
        assertFalse(underTest.render(renderContext, position, null, 0));
        assertTrue(underTest.render(renderContext, position, null, 1));
        assertTrue(underTest.render(renderContext, position, null, 5));
    }
    
    @Test
    public void testComparisonWithStrings() throws Exception {
        assertFalse(underTest.render(renderContext, position, null, ""));
        assertFalse(underTest.render(renderContext, position, null, "0"));
        assertTrue(underTest.render(renderContext, position, null, "1"));
        assertTrue(underTest.render(renderContext, position, null, "test"));
    }
}