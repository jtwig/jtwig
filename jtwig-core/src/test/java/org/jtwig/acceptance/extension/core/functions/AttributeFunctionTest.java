/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jtwig.acceptance.extension.core.functions;

import java.util.Collections;
import org.jtwig.JtwigModelMap;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.jtwig.JtwigTemplate.inlineTemplate;
import org.jtwig.exception.RenderException;
import org.jtwig.util.ObjectExtractor;
import org.mockito.Mockito;

public class AttributeFunctionTest {
    JtwigModelMap model = new JtwigModelMap(Collections.singletonMap("obj", (Object)new TestObject()));
    
    @Test
    public void generalTests() throws Exception {
        assertThat(
                inlineTemplate("{{ attribute(obj, 'noargs') }}").render(model),
                is(equalTo("hello, world")));
        assertThat(
                inlineTemplate("{{ attribute(obj, 'argd', ['thomas']) }}").render(model),
                is(equalTo("hello, thomas")));
        assertThat(
                inlineTemplate("{{ attribute(obj, 'var') }}").render(model),
                is(equalTo("test")));
    }
    
    @Test
    public void nonExistentOrInaccessibleMemberYieldsNull() throws Exception {
        assertThat(
                inlineTemplate("{{ attribute(obj, 'priv') }}").render(model),
                isEmptyString());
    }
    
    @Test(expected = RenderException.class)
    public void throwsErrorWithLessThanTwoArgs() throws Exception {
        inlineTemplate("{{ attribute(obj) }}").render();
    }
    
    @Test
    public void testExtractException() throws Exception {
        TestObject mock = spy(new TestObject());
        doThrow(ObjectExtractor.ExtractException.class).when(mock).getProtectedArg();
        JtwigModelMap model = new JtwigModelMap()
                .add("obj", mock);
        
        assertThat(inlineTemplate("{{ attribute(obj, 'protectedArg') }}").render(model),
                isEmptyString());
    }
    
    public static class TestObject {
        public String var = "test";
        private String priv = "bad";
        protected String protectedArg = "protected";
        
        public String noargs() {
            return "hello, world";
        }
        public String argd(String name) {
            return "hello, "+name;
        }
        public String getProtectedArg() {
            return protectedArg;
        }
    }
}