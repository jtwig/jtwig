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
package org.jtwig.acceptance.extension.core.tests;

import static org.jtwig.JtwigTemplate.inlineTemplate;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SameAsTestTest {
    @Test
    public void generalTests() throws Exception {
        assertEquals("0", inlineTemplate("{{ '5' is sameas(5) }}").render());
        assertEquals("1", inlineTemplate("{{ '5' is same as (\"5\") }}").render());
        assertEquals("0", inlineTemplate("{{ '5.0' is sameas(5) }}").render());
        assertEquals("0", inlineTemplate("{{ '5' is sameas(5.0) }}").render());
        assertEquals("0", inlineTemplate("{{ '5.0' is sameas(5.0) }}").render());
        assertEquals("1", inlineTemplate("{{ 5 is same as (5) }}").render());
        assertEquals("0", inlineTemplate("{{ 'false' is sameas(false) }}").render());
        assertEquals("1", inlineTemplate("{{ false is same as (false) }}").render());
        assertEquals("0", inlineTemplate("{{ 'true' is sameas(true) }}").render());
        assertEquals("0", inlineTemplate("{{ '' is sameas(null) }}").render());
    }
}