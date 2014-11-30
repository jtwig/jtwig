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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jtwig.parser.config.TagSymbols.JAVASCRIPT_COLLISION_FREE;
import static org.jtwig.util.SyntacticSugar.after;
import static org.jtwig.util.SyntacticSugar.given;

public class CustomTagsTest extends AbstractJtwigTest {
    @Test
    public void javascriptOutputTag() throws Exception {
        given(theConfiguration().parse().withSymbols(JAVASCRIPT_COLLISION_FREE));

        after(jtwigRenders(template("@> 1 <@")));
        assertThat(theRenderedTemplate(), is(equalTo("1")));
    }

    @Test
    public void javascriptCodeTag() throws Exception {
        given(theConfiguration().parse().withSymbols(JAVASCRIPT_COLLISION_FREE));

        after(jtwigRenders(template("<# if (true) #>Hello<# endif #>")));
        assertThat(theRenderedTemplate(), is(equalTo("Hello")));
    }

    @Test
    public void javascriptComment() throws Exception {
        given(theConfiguration().parse().withSymbols(JAVASCRIPT_COLLISION_FREE));

        after(jtwigRenders(template("<$ if (true) #>Hello<# endif $>")));
        assertThat(theRenderedTemplate(), is(equalTo("")));
    }
}
