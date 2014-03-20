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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ObjectExtractorTest {
    @Test
    public void shouldExtractFromMap () throws ObjectExtractor.ExtractException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", "value");
        ObjectExtractor underTest = new ObjectExtractor(map);

        assertThat(underTest.extract("key"), is((Object) "value"));
    }

    @Test
    public void shouldExtractFromInheritedMethod () throws ObjectExtractor.ExtractException {
        List<String> list = new ArrayList<String>();
        ObjectExtractor underTest = new ObjectExtractor(list);

        assertThat(underTest.extract("tostring"), is(notNullValue()));
    }
    @Test
    public void shouldExtractFromInheritedField () throws ObjectExtractor.ExtractException {
        B b = new B();
        b.a = "a";
        b.b = "b";
        ObjectExtractor underTest = new ObjectExtractor(b);

        assertThat(underTest.extract("a"), is((Object) "a"));
        assertThat(underTest.extract("b"), is((Object) "b"));
    }

    private static class A {
        public String a;
    }

    private static class B extends A {
        public String b;
    }
}
