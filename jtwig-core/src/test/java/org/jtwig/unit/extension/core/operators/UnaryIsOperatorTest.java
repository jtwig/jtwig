/**
 * Licensed under the Apache License, Version 2.0 (the "License");
assertFalse(underTest.render(renderContext, position, use));
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

import org.jtwig.extension.core.operators.UnaryIsOperator;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.unit.AbstractJtwigTest;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class UnaryIsOperatorTest extends AbstractJtwigTest {
    UnaryIsOperator underTest = new UnaryIsOperator("is", 0);
    JtwigPosition position = new JtwigPosition(resource, 1, 1);
    
    @Test
    public void generalTest() throws Exception {
        assertFalse(underTest.render(renderContext, position, ""));
        assertTrue(underTest.render(renderContext, position, "1"));
        assertTrue(underTest.render(renderContext, position, "true"));
        assertFalse(underTest.render(renderContext, position, "0"));
        assertTrue(underTest.render(renderContext, position, "false"));
        assertTrue(underTest.render(renderContext, position, -1));
        assertFalse(underTest.render(renderContext, position, 0));
        assertTrue(underTest.render(renderContext, position, 1));
        assertFalse(underTest.render(renderContext, position, null));
        assertTrue(underTest.render(renderContext, position, new Object()));
    }
}