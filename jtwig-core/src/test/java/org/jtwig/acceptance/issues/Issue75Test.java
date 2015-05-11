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

package org.jtwig.acceptance.issues;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

public class Issue75Test {
    @Test
    public void issue75IsNull() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("a", new NullPointer());

        String result = JtwigTemplate
            .inlineTemplate("{% if a.value is null %}A{% else %}B{% endif %}")
            .render(model);

        assertThat(result, containsString("A"));
    }

    @Test
    public void issue75IsNullWithParentheses() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("a", new NullPointer());

        String result = JtwigTemplate
            .inlineTemplate("{% if (a.value) is null %}A{% else %}B{% endif %}")
            .render(model);

        assertThat(result, containsString("A"));
    }

    public class NullPointer {
        public String getValue () {
            return null;
        }

        @Override
        public String toString() {
            return "Null Pointer";
        }
    }
}
