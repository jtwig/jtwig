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

package com.lyncode.jtwig.acceptance.issues;

import com.lyncode.jtwig.acceptance.AbstractJtwigTest;
import static com.lyncode.jtwig.util.SyntacticSugar.given;
import static com.lyncode.jtwig.util.SyntacticSugar.then;
import static com.lyncode.jtwig.util.SyntacticSugar.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;

/**
 * 
 */
public class Issue238Test extends AbstractJtwigTest {
    @Test
    public void canConcatenateConstants() throws Exception {
        when(jtwigRenders(template("{{ 'hello' ~ 'world' }}")));
        then(theRenderedTemplate(), is(equalTo("helloworld")));
    }
    @Test
    public void canConcatenateVariables() throws Exception {
        given(aModel()).withModelAttribute("a", "hello")
                .withModelAttribute("b", "world");
        when(jtwigRenders(template("{{ a ~ b }}")));
        then(theRenderedTemplate(), is(equalTo("helloworld")));
    }
}