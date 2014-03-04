/**
 * Copyright 2012 Lyncode
 *
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class WhitespaceControlTest extends AbstractJtwigTest {
    @Test
    public void ifSpaceControl() throws Exception {
        assertThat(theResultOfRendering(theTemplate(
                " {%- if true -%} A {%- endif -%} \n\t")),
                is(equalTo("A"))
        );
    }
    @Test
    public void elseSpaceControl() throws Exception {
        assertThat(theResultOfRendering(theTemplate(
                " {%- if false -%}{% else -%} A {% endif -%} \n\t")),
                is(equalTo("A "))
        );
    }
    @Test
    public void elseIfSpaceControl() throws Exception {
        assertThat(theResultOfRendering(theTemplate(
                " {%- if false -%}{% elseif true -%} A {% endif -%} \n\t")),
                is(equalTo("A "))
        );
    }
    @Test
    public void setTest() throws Exception {
        assertThat(theResultOfRendering(theTemplate(
                " {%- set a = 1 %}")),
                is(equalTo(""))
        );
    }
    @Test
    public void testOutput() throws Exception {
        assertThat(theResultOfRendering(theTemplate(
                " {{- 'hello' -}} ")),
                is(equalTo("hello"))
        );
    }
}
