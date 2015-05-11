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

package org.jtwig.unit.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtwig.render.RenderContext;
import org.jtwig.util.ObjectExtractor;
import org.junit.Test;
import org.mockito.Mockito;

public class ObjectExtractorTest {
    @Test
    public void shouldExtractFromMap () throws ObjectExtractor.ExtractException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", "value");
        ObjectExtractor underTest = new ObjectExtractor(Mockito.mock(RenderContext.class), map);

        assertThat(underTest.extract("key"), is((Object) "value"));
    }

    @Test
    public void shouldExtractFromInheritedMethod () throws ObjectExtractor.ExtractException {
        List<String> list = new ArrayList<String>();
        ObjectExtractor underTest = new ObjectExtractor(Mockito.mock(RenderContext.class), list);

        assertThat(underTest.extract("tostring"), is(notNullValue()));
    }
    @Test
    public void shouldExtractFromInheritedField () throws ObjectExtractor.ExtractException {
        B b = new B();
        b.a = "a";
        b.b = "b";
        ObjectExtractor underTest = new ObjectExtractor(Mockito.mock(RenderContext.class), b);

        assertEquals("a", underTest.extract("a"));
        assertEquals("b", underTest.extract("b"));
    }
	
	@Test
	public void shouldExtractFieldByGetter() throws ObjectExtractor.ExtractException {
		C c = new C();
		c.setValue(2);
		
		ObjectExtractor underTest = new ObjectExtractor(Mockito.mock(RenderContext.class), c);
		
		//direct
		assertEquals(2, underTest.extract("value"));
		
		//getter
		assertEquals(2, underTest.extract("valueM"));
	}

	@Test
	public void shouldExtractFromStaticMethod() throws ObjectExtractor.ExtractException
	{
		ObjectExtractor underTest = new ObjectExtractor(Mockito.mock(RenderContext.class), Integer.class);

		assertThat(underTest.extract("toHexString", 123), is((Object) "7b"));
	}

    @Test(expected = ObjectExtractor.ExtractException.class)
    public void methodException() throws Exception {
        ObjectExtractor underTest = new ObjectExtractor(Mockito.mock(RenderContext.class), new TestClass());

        underTest.extract("method");
    }

    public static class TestClass {
        public String method () {
            throw new RuntimeException();
        }
    }

    public static class A {
        public String a;
    }

    public static class B extends A {
        public String b;
    }
	
	public static class C {
		private int value;

		public int getValue() {
			return value;
		}
		
		@SuppressWarnings("unused")
		public int getValueM() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}
}
