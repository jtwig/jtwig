/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.unit.expressions.model;

import java.util.Map;
import org.jtwig.unit.AbstractJtwigTest;
import org.jtwig.expressions.api.Expression;
import org.jtwig.expressions.model.Constant;
import org.jtwig.expressions.model.ValueMap;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ValueMapTest extends AbstractJtwigTest {
    @Test
    public void test() throws Exception {
        ValueMap underTest = new ValueMap(null)
                .add("key", new Constant<>(null));

        Expression expression = underTest.compile(compileContext);
        assertNotNull(expression);

        Map map = (Map) expression.calculate(renderContext);
        assertTrue(map.containsKey("key"));
    }
}
