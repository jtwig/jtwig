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

package com.lyncode.jtwig.functions.internal.string;

import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

public class UrlEncodeTest {
    private UrlEncode underTest = new UrlEncode();

    @Test
    public void testExecute() throws Exception {
        assertEquals("path-seg%2Fment", underTest.execute("path-seg/ment"));
        assertEquals("foo+bar", underTest.execute("foo bar"));
        assertEquals("bar=a&foo=b", underTest.execute(new TreeMap<String, String>(){{
            put("bar", "a");
            put("foo", "b");
        }}));
    }
}
