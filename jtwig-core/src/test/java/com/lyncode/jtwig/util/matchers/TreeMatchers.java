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

package com.lyncode.jtwig.util.matchers;

import com.lyncode.jtwig.tree.content.Text;
import com.lyncode.jtwig.tree.expressions.FunctionElement;
import com.lyncode.jtwig.tree.expressions.Variable;
import com.lyncode.jtwig.tree.structural.Block;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class TreeMatchers {
    public static TypeSafeMatcher<Variable> variable (final Matcher<? super String> nameMatcher) {
        return new TypeSafeMatcher<Variable>() {
            @Override
            protected boolean matchesSafely(Variable item) {
                return nameMatcher.matches(item.getIdentifier());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("variable ").appendDescriptionOf(nameMatcher);
            }
        };
    }

    public static TypeSafeMatcher<FunctionElement> function (final Matcher<? super String> nameMatcher) {
        return new TypeSafeMatcher<FunctionElement>() {
            @Override
            protected boolean matchesSafely(FunctionElement item) {
                return nameMatcher.matches(item.getName());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("function ").appendDescriptionOf(nameMatcher);
            }
        };
    }

    public static TypeSafeMatcher<FunctionElement> function (final Matcher<? super String> nameMatcher, final ElementListMatcher<?> argumentsMatcher) {
        return new TypeSafeMatcher<FunctionElement>() {
            @Override
            protected boolean matchesSafely(FunctionElement item) {
                return nameMatcher.matches(item.getName()) && argumentsMatcher.matches(item.getArguments());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("function ").appendDescriptionOf(nameMatcher).appendText(" with parameters ").appendDescriptionOf(argumentsMatcher);
            }
        };
    }

    public static TypeSafeMatcher<Block> block (final Matcher<? super String> nameMatcher) {
        return new TypeSafeMatcher<Block>() {
            @Override
            protected boolean matchesSafely(Block item) {
                return nameMatcher.matches(item.getName());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Block with name ").appendDescriptionOf(nameMatcher);
            }
        };
    }

    public static TypeSafeMatcher<Text> text (final Matcher<? super String> textMatcher) {
        return new TypeSafeMatcher<Text>() {
            @Override
            protected boolean matchesSafely(Text item) {
                return textMatcher.matches(item.getText());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Text ").appendDescriptionOf(textMatcher);
            }
        };
    }
}
