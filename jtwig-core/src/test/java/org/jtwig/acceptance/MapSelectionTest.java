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

package org.jtwig.acceptance;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.jtwig.configuration.JtwigConfiguration;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MapSelectionTest extends AbstractJtwigTest {
    @Test
    public void testTrue() throws Exception {
        JtwigTemplate template = new JtwigTemplate("{{ test['abc'] }}", new JtwigConfiguration());
        JtwigModelMap context = new JtwigModelMap();
        context.add("test", new HashMap<String, String>() {{
            put("abc", "a");
        }});
        String result = template.output(context);
        assertThat(result, is("a"));
    }
}
