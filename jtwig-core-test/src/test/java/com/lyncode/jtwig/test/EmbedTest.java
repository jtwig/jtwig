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

package com.lyncode.jtwig.test;

import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import org.junit.Test;

import static com.lyncode.jtwig.SyntacticSugar.after;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class EmbedTest extends AbstractJtwigTest {
    @Test
    public void emptyEmbed() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(templateResource("embed/empty.twig")));
        assertThat(theRenderedTemplate(), containsString("1/1"));
    }

    @Test
    public void partialOverrideEmbed() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(templateResource("embed/partialOverride.twig")));
        assertThat(theRenderedTemplate(), containsString("1/2"));
    }

    @Test
    public void fullOverrideEmbed() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(templateResource("embed/fullOverride.twig")));
        assertThat(theRenderedTemplate(), containsString("2/2"));
    }

    @Test
    public void nestedOverrideEmbed() throws ParseException, CompileException, RenderException {
        after(jtwigRenders(templateResource("embed/nestedOverride.twig")));
        assertThat(theRenderedTemplate(), containsString("1/(4/2)"));
    }
}
