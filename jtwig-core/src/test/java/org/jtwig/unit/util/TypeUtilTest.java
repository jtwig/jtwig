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
import java.util.Arrays;
import java.util.Collections;
import org.jtwig.types.Undefined;
import org.jtwig.util.TypeUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeUtilTest {
    @Test
    public void ensureToBooleanIsTwigCompliant() throws Exception {
        new TypeUtil(); // Satisfy JaCoCo
        assertFalse(TypeUtil.toBoolean(null));
        assertFalse(TypeUtil.toBoolean(Undefined.UNDEFINED));
        assertTrue(TypeUtil.toBoolean(true));
        assertFalse(TypeUtil.toBoolean(false));
        assertFalse(TypeUtil.toBoolean(0));
        assertTrue(TypeUtil.toBoolean(1));
        assertTrue(TypeUtil.toBoolean(-1));
        assertFalse(TypeUtil.toBoolean(0D));
        assertTrue(TypeUtil.toBoolean(-0.1D));
        assertTrue(TypeUtil.toBoolean(0.1D));
        assertFalse(TypeUtil.toBoolean(Collections.EMPTY_LIST));
        assertTrue(TypeUtil.toBoolean(Arrays.asList(new Object())));
        assertFalse(TypeUtil.toBoolean(Collections.EMPTY_MAP));
        assertTrue(TypeUtil.toBoolean(Collections.singletonMap("test", "test")));
        assertFalse(TypeUtil.toBoolean(new String[0]));
        assertTrue(TypeUtil.toBoolean(new String[]{"test"}));
        assertTrue(TypeUtil.toBoolean(new Object()));
        assertTrue(TypeUtil.toBoolean("true"));
        assertTrue(TypeUtil.toBoolean("false"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void ensureToLongIsTwigCompliant() throws Exception {
        assertEquals(0L, (long)TypeUtil.toLong(null));
        assertEquals(0L, (long)TypeUtil.toLong(Undefined.UNDEFINED));
        assertEquals(0L, (long)TypeUtil.toLong(false));
        assertEquals(1L, (long)TypeUtil.toLong(true));
        assertEquals(0L, (long)TypeUtil.toLong(""));
        assertEquals(0L, (long)TypeUtil.toLong("test"));
        assertEquals(75L, (long)TypeUtil.toLong("75test"));
        assertEquals(0L, (long)TypeUtil.toLong("test75"));
        assertEquals(75L, (long)TypeUtil.toLong("75"));
        assertEquals(5L, (long)TypeUtil.toLong((short)5));
        assertEquals(7L, (long)TypeUtil.toLong(7.5D));
        assertEquals(1L, (long)TypeUtil.toLong(new Object()));
        TypeUtil.toLong(Collections.EMPTY_LIST);
    }
    
    @Test
    public void ensureIsLongIsStrict() throws Exception {
        assertFalse(TypeUtil.isLong(null));
        assertFalse(TypeUtil.isLong(null));
        assertFalse(TypeUtil.isLong(null));
        assertFalse(TypeUtil.isLong(""));
        assertFalse(TypeUtil.isLong("test"));
        assertFalse(TypeUtil.isLong("test75"));
        assertTrue(TypeUtil.isLong("75test"));
        assertTrue(TypeUtil.isLong("75"));
        assertFalse(TypeUtil.isLong("7.5"));
        assertFalse(TypeUtil.isLong(1D));
        assertFalse(TypeUtil.isLong(1F));
        assertTrue(TypeUtil.isLong(5L));
        assertTrue(TypeUtil.isLong(5));
        assertTrue(TypeUtil.isLong((short)5));
        assertTrue(TypeUtil.isLong((byte)5));
        assertFalse(TypeUtil.isLong(new Object()));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void ensureToDecimalIsTwigCompliant() throws Exception {
        assertEquals(BigDecimal.ZERO, TypeUtil.toDecimal(null));
        assertEquals(BigDecimal.ZERO, TypeUtil.toDecimal(Undefined.UNDEFINED));
        assertEquals(BigDecimal.ZERO, TypeUtil.toDecimal(false));
        assertEquals(BigDecimal.ONE, TypeUtil.toDecimal(true));
        assertEquals(BigDecimal.ZERO, TypeUtil.toDecimal(""));
        assertEquals(BigDecimal.ZERO, TypeUtil.toDecimal("test"));
        assertEquals(new BigDecimal("7.5"), TypeUtil.toDecimal("7.5test"));
        assertEquals(BigDecimal.ZERO, TypeUtil.toDecimal("test7.5"));
        assertEquals(new BigDecimal("7.5"), TypeUtil.toDecimal("7.5"));
        assertEquals(new BigDecimal("5"), TypeUtil.toDecimal((short)5));
        assertEquals(new BigDecimal("7.5"), TypeUtil.toDecimal(7.5D));
        assertEquals(BigDecimal.ONE, TypeUtil.toDecimal(new Object()));
        TypeUtil.toDecimal(Collections.EMPTY_LIST);
    }
    
    @Test
    public void ensureIsDecimalIsStrict() throws Exception {
        assertFalse(TypeUtil.isDecimal(null));
        assertFalse(TypeUtil.isDecimal(Undefined.UNDEFINED));
        assertFalse(TypeUtil.isDecimal(false));
        assertFalse(TypeUtil.isDecimal(true));
        assertFalse(TypeUtil.isDecimal(""));
        assertFalse(TypeUtil.isDecimal("test"));
        assertTrue(TypeUtil.isDecimal("7.5test"));
        assertFalse(TypeUtil.isDecimal("test7.5"));
        assertTrue(TypeUtil.isDecimal("7.5"));
        assertFalse(TypeUtil.isDecimal("75"));
        assertTrue(TypeUtil.isDecimal(7.5F));
        assertTrue(TypeUtil.isDecimal(7.5D));
        assertFalse(TypeUtil.isDecimal(new Object()));
    }
    
    @Test
    public void ensureAreDecimalIsAccurate() throws Exception {
        assertTrue(TypeUtil.areDecimal(new Object[]{"7.5", 7.5D, 7.5F}));
        assertTrue(TypeUtil.areDecimal("7.5", 7.5D, 7.5F));
        assertFalse(TypeUtil.areDecimal(new Object[]{"test", 7.5D, 7.5F}));
        assertFalse(TypeUtil.areDecimal("test", 7.5D, 7.5F));
        assertFalse(TypeUtil.areDecimal());
        assertFalse(TypeUtil.areDecimal(new Object[0]));
    }
    
    @Test
    public void ensureStringToNumberWorks() throws Exception {
        assertEquals(new BigDecimal("7.5"), TypeUtil.toNumber("7.5"));
        assertEquals(new BigDecimal("7.5"), TypeUtil.toNumber("7.5test"));
        assertEquals(0L, TypeUtil.toNumber("test7.5"));
        assertEquals(0L, TypeUtil.toNumber("test"));
        assertEquals(7L, (long)TypeUtil.toNumber("7"));
        assertEquals(7L, (long)TypeUtil.toNumber("7test"));
        assertEquals(0L, TypeUtil.toNumber("test7"));
        assertEquals(0L, TypeUtil.toNumber("test"));
        assertEquals(0L, TypeUtil.toNumber(""));
    }
    
    @Test
    public void ensureIsNumericWorks() throws Exception {
        assertTrue(TypeUtil.isNumeric(null));
        assertTrue(TypeUtil.isNumeric(null));
        assertTrue(TypeUtil.isNumeric(true));
        assertTrue(TypeUtil.isNumeric(false));
        assertTrue(TypeUtil.isNumeric(""));
        assertTrue(TypeUtil.isNumeric(5));
        assertTrue(TypeUtil.isNumeric(5D));
        assertFalse(TypeUtil.isNumeric(new Object()));
    }
    
    @Test
    public void ensureAreLooselyEqualWorks() throws Exception {
        // Check nulls
        assertTrue(TypeUtil.areLooselyEqual(null, null));
        assertTrue(TypeUtil.areLooselyEqual(null, 0));
        assertFalse(TypeUtil.areLooselyEqual(null, 1));
        assertFalse(TypeUtil.areLooselyEqual(null, "0"));
        assertFalse(TypeUtil.areLooselyEqual(null, "1"));
        assertTrue(TypeUtil.areLooselyEqual(null, false));
        assertFalse(TypeUtil.areLooselyEqual(null, true));
        assertFalse(TypeUtil.areLooselyEqual(null, new Object()));
        assertTrue(TypeUtil.areLooselyEqual(null, ""));
        assertTrue(TypeUtil.areLooselyEqual("", null));
        
        // Check booleans
        assertFalse(TypeUtil.areLooselyEqual(false, true));
        assertTrue(TypeUtil.areLooselyEqual(false, 0));
        assertFalse(TypeUtil.areLooselyEqual(false, 1));
        assertFalse(TypeUtil.areLooselyEqual(false, -1));
        assertTrue(TypeUtil.areLooselyEqual(false, "0"));
        assertFalse(TypeUtil.areLooselyEqual(false, "1"));
        assertFalse(TypeUtil.areLooselyEqual(false, "-1"));
        assertFalse(TypeUtil.areLooselyEqual(false, "false"));
        assertFalse(TypeUtil.areLooselyEqual(false, new Object()));
        assertFalse(TypeUtil.areLooselyEqual(true, 0));
        assertTrue(TypeUtil.areLooselyEqual(true, 1));
        assertTrue(TypeUtil.areLooselyEqual(true, -1));
        assertFalse(TypeUtil.areLooselyEqual(true, "0"));
        assertTrue(TypeUtil.areLooselyEqual(true, "1"));
        assertTrue(TypeUtil.areLooselyEqual(true, "-1"));
        assertTrue(TypeUtil.areLooselyEqual(true, "false"));
        assertTrue(TypeUtil.areLooselyEqual(true, new Object()));
    }
}