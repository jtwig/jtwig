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
import org.jtwig.AbstractJtwigTest;
import org.junit.Test;

public class WhitespaceControlTest extends AbstractJtwigTest {
    @Test
    public void ifSpaceControl() throws Exception {
        assertThat(theResultOf(theTemplate(
                " {%- if true -%} A {%- endif -%} \n\t")),
                   is(equalTo("A"))
        );
    }

    @Test
    public void elseSpaceControl() throws Exception {
        assertThat(theResultOf(theTemplate(
                " {%- if false -%}{% else -%} A {% endif -%} \n\t")),
                   is(equalTo("A "))
        );
    }

    @Test
    public void elseIfSpaceControl() throws Exception {
        assertThat(theResultOf(theTemplate(
                " {%- if false -%}{% elseif true -%} A {% endif -%} \n\t")),
                   is(equalTo("A "))
        );
    }

    @Test
    public void setTest() throws Exception {
        assertThat(theResultOf(theTemplate(
                " {%- set a = 1 %}")),
                   is(equalTo(""))
        );
    }

    @Test
    public void testOutput() throws Exception {
        assertThat(theResultOf(theTemplate(
                " {{- 'hello' -}} ")),
                   is(equalTo("hello"))
        );
    }

    @Test
    public void testComment() throws Exception {
        assertThat(theResultOf(theTemplate(
                " {#- 'hello' -#} ")),
                   is(equalTo(""))
        );
    }

    @Test
    public void testCommentEmpty() throws Exception {
        assertThat(theResultOf(theTemplate(
                " {#--#} ")),
                   is(equalTo(""))
        );
    }

    @Test
    public void testCommentRight() throws Exception {
        assertThat(theResultOf(theTemplate(
                " {# Hola -#} ")),
                   is(equalTo(" "))
        );
    }

    @Test
    public void testCommentLeft() throws Exception {
        assertThat(theResultOf(theTemplate(
                " {#- Hola #} ")),
                   is(equalTo(" "))
        );
    }
}
