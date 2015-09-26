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

import org.jtwig.extension.core.operators.UnaryNegativeOperator;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.unit.AbstractJtwigTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnaryNegativeOperatorTest extends AbstractJtwigTest {
    UnaryNegativeOperator underTest = new UnaryNegativeOperator("-", 0);
    JtwigPosition position = new JtwigPosition(resource, 1, 1);
    
    @Test
    public void generalTests() throws Exception {
        assertEquals(0L, underTest.render(renderContext, position, ""));
        assertEquals(-1L, underTest.render(renderContext, position, "1"));
        assertEquals(0L, underTest.render(renderContext, position, "true"));
        assertEquals(0L, underTest.render(renderContext, position, "0"));
        assertEquals(0L, underTest.render(renderContext, position, "false"));
        assertEquals(-1L, underTest.render(renderContext, position, 1));
        assertEquals(0L, underTest.render(renderContext, position, 0));
        assertEquals(0L, underTest.render(renderContext, position, null));
        assertEquals(-1L, underTest.render(renderContext, position, new Object()));
    }
}