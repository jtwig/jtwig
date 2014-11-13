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

package com.lyncode.jtwig.acceptance.functions;

import com.lyncode.jtwig.acceptance.AbstractJtwigTest;
import static com.lyncode.jtwig.util.SyntacticSugar.given;
import static com.lyncode.jtwig.util.SyntacticSugar.then;
import static com.lyncode.jtwig.util.SyntacticSugar.when;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import org.junit.Test;

public class BlockFunctionTest extends AbstractJtwigTest {
    @Test
    public void ensureBlockFunctionWorksWithVariables() throws Exception {
        given(aModel().withModelAttribute("var", "title"));
        when(jtwigRenders(templateResource("templates/acceptance/block/tested.twig")));
        then(theRenderedTemplate(), is(equalTo("title!")));
    }
    @Test
    public void ensureBlockFunctionWorksWithVariables2() throws Exception {
        given(aModel().withModelAttribute("var", "body"));
        when(jtwigRenders(templateResource("templates/acceptance/block/tested.twig")));
        then(theRenderedTemplate(), is(equalTo("body!")));
    }
}