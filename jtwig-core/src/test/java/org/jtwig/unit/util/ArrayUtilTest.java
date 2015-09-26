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

package org.jtwig.unit.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static org.jtwig.util.ArrayUtil.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.jtwig.util.ArrayUtil;
import org.junit.Test;

public class ArrayUtilTest {
    @Test
    public void testRange() throws Exception {
        new ArrayUtil(); // Satisfy JaCoCo
        assertArrayEquals(new Character[]{'a','b','c'}, (Character[])range("a", "c"));

        assertArrayEquals(new Long[]{1L,2L,3L}, (Long[])range(1, 3));
        assertArrayEquals(new Long[]{1L,2L,3L}, (Long[])range("1", 3));
        assertArrayEquals(new Long[]{1L,2L,3L}, (Long[])range(1, "3"));
        assertArrayEquals(new Long[]{1L,2L,3L}, (Long[])range("1", "3"));

        assertArrayEquals(new Long[]{0L,1L,2L,3L}, (Long[])range("a", 3));
        assertArrayEquals(new Long[]{1L,0L}, (Long[])range(1, "c"));

        assertArrayEquals(new Long[]{0L,1L,2L,3L}, (Long[])range(null, 3));
        assertArrayEquals(new Long[]{3L,2L,1L,0L}, (Long[])range(3, null));
        assertArrayEquals(new Long[]{0L}, (Long[])range(null, "c"));
        assertArrayEquals(new Long[]{0L}, (Long[])range("c", null));

        assertArrayEquals(new Long[]{1L,2L,3L}, (Long[])range(new Object(), 3));
        assertArrayEquals(new Long[]{3L,2L,1L}, (Long[])range(3, new Object()));
        assertArrayEquals(new Long[]{1L,0L}, (Long[])range(new Object(), "c"));
        assertArrayEquals(new Long[]{0L,1L}, (Long[])range("c", new Object()));

        assertNull(range(1.1, 1.3));
        assertArrayEquals(new BigDecimal[]{new BigDecimal("1.1"), new BigDecimal("2.1")}, (BigDecimal[])range(1.1, 2.3));
        assertArrayEquals(new BigDecimal[]{new BigDecimal("1.1"), new BigDecimal("2.1")}, (BigDecimal[])range("1.1", 2.3));
        assertArrayEquals(new BigDecimal[]{new BigDecimal("1.1"), new BigDecimal("0.1")}, (BigDecimal[])range("1.1", "d"));

        assertArrayEquals(new BigDecimal[]{new BigDecimal("3.1"), new BigDecimal("4.3"), new BigDecimal("5.5"), new BigDecimal("6.7")}, (BigDecimal[])range(3.1, 7.2, 1.2));
        assertArrayEquals(new Character[]{'Z','[','\\',']','^','_','`','a'}, (Character[])range("Z", "a"));
        assertArrayEquals(new Long[]{0L}, (Long[])range("AZ", ""));
        
        assertNull(range(1,2,3));
        assertNull(range('a','b',3));
    }
    
    @Test
    public void testPrimitiveArrays() throws Exception {
        assertThat(toArray((Object)new byte[]{0xe}),
                is(arrayContaining((Object)(byte)0xe)));
        assertThat(toArray((Object)new short[]{5}),
                is(arrayContaining((Object)(short)5)));
        assertThat(toArray((Object)new int[]{5}),
                is(arrayContaining((Object)(int)5)));
        assertThat(toArray((Object)new long[]{5L}),
                is(arrayContaining((Object)(long)5)));
        assertThat(toArray((Object)new boolean[]{false}),
                is(arrayContaining((Object)false)));
        assertThat(toArray((Object)new char[]{0xe}),
                is(arrayContaining((Object)(char)0xe)));
        assertThat(toArray((Object)new float[]{1F}),
                is(arrayContaining((Object)(float)1)));
        assertThat(toArray((Object)new double[]{1D}),
                is(arrayContaining((Object)(double)1)));
    }
    
    @Test
    public void testObjectArrays() throws Exception {
        assertTrue(toArray(new Object[]{new Object()}) instanceof Object[]);
        assertFalse(toArray(new Object[]{new Object()}).getClass().getComponentType().isPrimitive());
        
        assertTrue(toArray((Object)new Object[]{new Object()}) instanceof Object[]);
        assertFalse(toArray((Object)new Object[]{new Object()}).getClass().getComponentType().isPrimitive());
        
        assertThat(toArray((Object)new Byte[]{0xe}),
                is(arrayContaining((Object)(byte)0xe)));
        assertThat(toArray((Object)new Short[]{5}),
                is(arrayContaining((Object)(short)5)));
        assertThat(toArray((Object)new Integer[]{5}),
                is(arrayContaining((Object)(int)5)));
        assertThat(toArray((Object)new Long[]{5L}),
                is(arrayContaining((Object)(long)5)));
        assertThat(toArray((Object)new Boolean[]{false}),
                is(arrayContaining((Object)false)));
        assertThat(toArray((Object)new Character[]{0xe}),
                is(arrayContaining((Object)(char)0xe)));
        assertThat(toArray((Object)new Float[]{1F}),
                is(arrayContaining((Object)(float)1)));
        assertThat(toArray((Object)new Double[]{1D}),
                is(arrayContaining((Object)(double)1)));
        assertThat(toArray((Object)"test"),
                is(arrayContaining((Object)"test")));
        assertThat(toArray((Object)new BigInteger[]{new BigInteger("1")}),
                is(arrayContaining((Object)new BigInteger("1"))));
    }
    
    @Test
    public void testCollectionsAndMapsToArrays() throws Exception {
        assertThat(toArray((Object)Arrays.asList("a","b","c")),
                is(arrayContaining((Object)"a","b","c")));
        Map<String, String> map = new HashMap<>();
        map.put("a", "apple");
        map.put("b", "banana");
        assertThat(toArray((Object)map),
                is(arrayContainingInAnyOrder((Object)"apple", "banana")));
    }
    
    @Test
    public void testToList() throws Exception {
        assertThat(toList(new String[]{"a","b"}),
                hasSize(2));
        assertThat(toList(Arrays.asList("a","b")),
                hasSize(2));
        assertThat(toList(new HashMap(){{
            put("a","b");
            put("c","d");
        }}),
                hasSize(2));
    }
}