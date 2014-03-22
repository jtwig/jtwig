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

import org.junit.Test;

import static com.lyncode.jtwig.SyntacticSugar.after;
import static com.lyncode.jtwig.SyntacticSugar.given;
import static com.lyncode.jtwig.parser.config.ParserConfiguration.configuration;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CustomTagsTest extends AbstractJtwigTest {
    @Test
    public void customOutputTag() throws Exception {
        given(theParser().withConfiguration(
                configuration()
                .withBeginOutput("(>")
                .withEndOutput("<)")
        ));

        after(jtwigRenders(template("(> 1 <)")));
        assertThat(theRenderedTemplate(), is(equalTo("1")));
    }

    @Test
    public void customCodeTag() throws Exception {
        given(theParser().withConfiguration(
                configuration()
                        .withBeginCode("(&")
                        .withEndCode("&)")
        ));

        after(jtwigRenders(template("(& if (true) &)Hello(& endif &)")));
        assertThat(theRenderedTemplate(), is(equalTo("Hello")));
    }
}
