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

import org.jtwig.extension.core.operators.BinaryLessThanOrEqualOperator;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.unit.AbstractJtwigTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryLessThanOrEqualOperatorTest extends AbstractJtwigTest {
    BinaryLessThanOrEqualOperator underTest = new BinaryLessThanOrEqualOperator("<=", 0);
    JtwigPosition position = new JtwigPosition(resource, 1, 1);
    
    @Test
    public void generalTest() throws Exception {
        assertFalse(underTest.render(renderContext, position, 2, 1));
        assertTrue(underTest.render(renderContext, position, 2, 2));
        assertTrue(underTest.render(renderContext, position, 2, 3));
        assertTrue(underTest.render(renderContext, position, 1, "2"));
        assertTrue(underTest.render(renderContext, position, 1, "2.1"));
        assertTrue(underTest.render(renderContext, position, "1", "2.1"));
        assertTrue(underTest.render(renderContext, position, 0, null));
        assertFalse(underTest.render(renderContext, position, 1, null));
        assertTrue(underTest.render(renderContext, position, null, 1));
        assertTrue(underTest.render(renderContext, position, null, 2));
        assertTrue(underTest.render(renderContext, position, new Object(), 1));
    }
}