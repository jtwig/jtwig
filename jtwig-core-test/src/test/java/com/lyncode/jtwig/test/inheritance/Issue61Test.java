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

package com.lyncode.jtwig.test.inheritance;

import com.lyncode.jtwig.test.AbstractJtwigTest;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import static com.lyncode.jtwig.SyntacticSugar.after;
import static org.hamcrest.MatcherAssert.assertThat;

public class Issue61Test extends AbstractJtwigTest {

    @Test
    public void issue61() throws Exception {
        after(jtwigRenders(templateResource("templates/issue61/final.twig")));
        assertThat(theRenderedTemplate(), containsString("some content"));
    }

    // Don't know why maven compilation is not recognizing the containsString from CoreMatchers...
    private Matcher<String> containsString(final String string) {
        return new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String item) {
                return item.contains(string);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("contains ").appendValue(string);
            }
        };
    }
}
