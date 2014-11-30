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

import org.jtwig.JtwigModelMap;
import org.jtwig.expressions.model.Variable;
import org.jtwig.render.RenderContext;
import org.jtwig.render.config.RenderConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VariableTest {
    private Variable underTest = new Variable(null, "variable");

    @Test
    public void resolveValueTest() throws Exception {
        JtwigModelMap modelMap = new JtwigModelMap()
                .withModelAttribute("variable", "one")
                ;
        assertEquals("one", underTest.compile(null).calculate(RenderContext.create(new RenderConfiguration(), modelMap, null)));
    }
}
