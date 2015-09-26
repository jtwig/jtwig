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
package org.jtwig.acceptance.extension.core.functions;

import java.util.Arrays;
import static org.hamcrest.Matchers.*;
import org.jtwig.JtwigModelMap;
import static org.jtwig.JtwigTemplate.inlineTemplate;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class DumpFunctionTest {
    @Test
    public void generalTests() throws Exception {
        JtwigModelMap model = new JtwigModelMap()
                .add("obj", new TestObj());
        
        assertThat(inlineTemplate("{{ dump(obj) }}").render(model),
                stringContainsInOrder(Arrays.asList(
                        "org.jtwig.acceptance.extension.core.functions.DumpFunctionTest$TestObj",
                        "var1=hello",
                        "var2=world"
                ))
        );
    }
    
    public static class TestObj {
        private String var1 = "hello";
        private String var2 = "world";
    }
}