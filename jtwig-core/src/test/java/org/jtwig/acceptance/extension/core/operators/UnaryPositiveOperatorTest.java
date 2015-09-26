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

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.jtwig.JtwigTemplate.inlineTemplate;

public class UnaryPositiveOperatorTest {
    @Test
    public void generalTests() throws Exception {
        assertThat(inlineTemplate("{{ +(-5) }}").render(),
                is(equalTo("5")));
        assertThat(inlineTemplate("{{ +(-5.2) }}").render(),
                is(equalTo("5.2")));
        assertThat(inlineTemplate("{{ +null }}").render(),
                is(equalTo("0")));
    }
}