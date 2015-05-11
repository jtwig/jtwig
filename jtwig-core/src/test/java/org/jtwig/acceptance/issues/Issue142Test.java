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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jtwig.util.SyntacticSugar.given;
import static org.jtwig.util.SyntacticSugar.then;

public class Issue142Test {
    @Test
    public void longFirstArgEvaluation() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("var", 5L);

        String result = JtwigTemplate
            .inlineTemplate("{{ var == 5 }}")
            .render(model);

        assertThat(result, is(equalTo("1")));
    }

    @Test
    public void longSecondArgEvaluation() throws Exception {
        JtwigModelMap model = new JtwigModelMap();
        model.withModelAttribute("var", 5L);

        String result = JtwigTemplate
            .inlineTemplate("{{ 5 == var }}")
            .render(model);

        assertThat(result, is(equalTo("1")));
    }
}
