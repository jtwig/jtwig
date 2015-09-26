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
package org.jtwig.acceptance.extension.core.operators;

import java.util.Collections;
import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Assert;
import org.junit.Test;

public class BinaryNotEqualOperator {
    
    @Test
    public void generalTests() throws Exception {
        Assert.assertEquals("1", JtwigTemplate.inlineTemplate("{{ 0 != 1 }}").render());
        Assert.assertEquals("1", JtwigTemplate.inlineTemplate("{{ obj.attr != '' }}")
                .render(new JtwigModelMap().withModelAttribute("obj", Collections.singletonMap("attr", "test"))));
    }
    
}