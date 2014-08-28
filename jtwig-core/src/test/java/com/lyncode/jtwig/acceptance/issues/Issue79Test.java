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

package com.lyncode.jtwig.acceptance.issues;

import com.lyncode.jtwig.acceptance.AbstractJtwigTest;
import org.junit.Test;

import static com.lyncode.jtwig.util.SyntacticSugar.given;
import static com.lyncode.jtwig.util.SyntacticSugar.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Issue79Test extends AbstractJtwigTest {
    @Test
    public void notDefinedVariable() throws Exception {
        when(jtwigRenders(template("{% if a is not defined %}A{% endif %}")));
        assertThat(theRenderedTemplate(), is(equalTo("A")));
    }

    @Test
    public void notDefinedMethod() throws Exception {
        given(aModel().withModelAttribute("a", "test"));
        when(jtwigRenders(template("{% if (a.check) is not defined %}A{% endif %}")));
        assertThat(theRenderedTemplate(), is(equalTo("A")));
    }
}
