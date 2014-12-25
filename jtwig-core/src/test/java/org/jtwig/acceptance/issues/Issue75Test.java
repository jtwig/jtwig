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

import static org.hamcrest.core.StringContains.containsString;
import org.jtwig.AbstractJtwigTest;
import static org.jtwig.util.SyntacticSugar.given;
import static org.jtwig.util.SyntacticSugar.then;
import org.junit.Test;

public class Issue75Test extends AbstractJtwigTest {
    @Test
    public void issue75IsNull() throws Exception {
        given(theModel().withModelAttribute("a", new NullPointer()));
        withResource("{% if a.value is null %}A{% else %}B{% endif %}");
        then(theResult(), containsString("A"));
    }


    @Test
    public void issue75IsNullWithParentheses() throws Exception {
        given(theModel().withModelAttribute("a", new NullPointer()));
        withResource("{% if (a.value) is null %}A{% else %}B{% endif %}");
        then(theResult(), containsString("A"));
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
