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

package com.lyncode.jtwig.util;

import org.junit.Test;

import static com.lyncode.jtwig.functions.util.HtmlUtils.stripTags;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HtmlUtilsTest {
    @Test
    public void removeCommentsTest() throws Exception {
        assertThat(stripTags("<!-- <a href='test'></a>-->", ""), is(equalTo("")));
        assertThat(stripTags("<!-- -->", ""), is(equalTo("")));
        assertThat(stripTags("<!-- -->a", ""), is(equalTo("a")));
        assertThat(stripTags("<!-- -->a<!--asd-->", ""), is(equalTo("a")));
    }


    @Test
    public void withoutAllowedTags() throws Exception {
        assertThat(stripTags("<a>Hello</a>"), is(equalTo("Hello")));
        assertThat(stripTags("<a\nhref='asd'>Hello</a>"), is(equalTo("Hello")));
        assertThat(stripTags("<a href='asd'>Hello</a>. Joao <a>Melo</a>"), is(equalTo("Hello. Joao Melo")));
    }

    public void withAllowedTags () {
        assertThat(stripTags("<a href='asd'>Hello</a>", "<a>"), is(equalTo("<a href='asd'>Hello</a>")));
        assertThat(stripTags("<a href='asd'\n>Hello</a>", "<a>"), is(equalTo("<a href='asd'\n>Hello</a>")));
    }
}
