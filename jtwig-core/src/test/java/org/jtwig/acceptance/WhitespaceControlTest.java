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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

public class WhitespaceControlTest {
    @Test
    public void ifSpaceControl() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate(" {%- if true -%} A {%- endif -%} \n\t")
            .render(model);

        assertThat(result, is(equalTo("A")));
    }

    @Test
    public void elseSpaceControl() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate(" {%- if false -%}{% else -%} A {% endif -%} \n\t")
            .render(model);

        assertThat(result, is(equalTo("A ")));
    }

    @Test
    public void elseIfSpaceControl() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate(" {%- if false -%}{% elseif true -%} A {% endif -%} \n\t")
            .render(model);

        assertThat(result, is(equalTo("A ")));
    }

    @Test
    public void setTest() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate(" {%- set a = 1 %}")
            .render(model);

        assertThat(result, is(equalTo("")));
    }

    @Test
    public void testOutput() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate(" {{- 'hello' -}} ")
            .render(model);

        assertThat(result, is(equalTo("hello")));
    }

    @Test
    public void testComment() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate(" {#- 'hello' -#} ")
            .render(model);

        assertThat(result, is(equalTo("")));
    }

    @Test
    public void testCommentEmpty() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate(" {#--#} ")
            .render(model);

        assertThat(result, is(equalTo("")));
    }

    @Test
    public void testCommentRight() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate(" {# Hola -#} ")
            .render(model);

        assertThat(result, is(equalTo(" ")));
    }

    @Test
    public void testCommentLeft() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate
            .inlineTemplate(" {#- Hola #} ")
            .render(model);

        assertThat(result, is(equalTo(" ")));
    }
}
