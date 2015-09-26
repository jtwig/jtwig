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

import org.jtwig.extension.core.operators.BinaryConcatenationOperator;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.unit.AbstractJtwigTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryConcatenationOperatorTest extends AbstractJtwigTest {
    BinaryConcatenationOperator underTest = new BinaryConcatenationOperator("~", 0);
    JtwigPosition position = new JtwigPosition(resource, 1, 1);
    
    @Test
    public void generalTest() throws Exception {
        assertEquals("15", underTest.render(renderContext, position, 1, 5));
        assertEquals("45", underTest.render(renderContext, position, 4, 5));
        assertEquals("5test", underTest.render(renderContext, position, 5, "test"));
        assertEquals("test5", underTest.render(renderContext, position, "test", "5"));
        assertTrue(underTest.render(renderContext, position, "test", new Object()).toString().startsWith("testjava.lang.Object@")); // Twig craps out doing this, but we don't need to
    }
}