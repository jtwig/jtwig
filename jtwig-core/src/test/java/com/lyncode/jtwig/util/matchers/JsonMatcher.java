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

import com.jayway.jsonpath.JsonPath;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.IOException;

public class JsonMatcher extends TypeSafeMatcher<String> {
    public static JsonMatcher json (String path, Matcher<String> value) {
        return new JsonMatcher(path, value);
    }

    private final ObjectMapper mapper = new ObjectMapper();
    private final String path;
    private final Matcher<String> matcher;

    public JsonMatcher(String path, Matcher<String> matcher) {
        this.path = path;
        this.matcher = matcher;
        mapper.setVisibility(JsonMethod.ALL, JsonAutoDetect.Visibility.ANY);
    }

    @Override
    protected boolean matchesSafely(String item) {
        return matcher.matches(JsonPath.compile(path).read(item));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("json path ").appendValue(path).appendText(" ").appendDescriptionOf(matcher);
    }

    @Override
    protected void describeMismatchSafely(String item, Description mismatchDescription) {
        try {
            mismatchDescription.appendText("was ").appendValue(mapper.writeValueAsString(JsonPath.compile(path).read(item)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
