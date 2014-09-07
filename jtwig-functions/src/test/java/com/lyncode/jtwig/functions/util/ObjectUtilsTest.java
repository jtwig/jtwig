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

package com.lyncode.jtwig.functions.util;

import org.junit.Test;

import org.junit.Assert;

public class ObjectUtilsTest {
    @Test
    public void compareLowercaseToUppercase() {
        Assert.assertEquals(0, ObjectUtils.compare('a', 'a'));
        Assert.assertEquals(1, ObjectUtils.compare('a', 'B'));
        Assert.assertEquals(-1, ObjectUtils.compare('a', 'b'));
    }
    @Test
    public void compareNumbersToAlpha() {
        Assert.assertEquals(1, ObjectUtils.compare(1, 'z'));
        Assert.assertEquals(1, ObjectUtils.compare(1.2, "zulu"));
        Assert.assertEquals(-1, ObjectUtils.compare("aurora", 5));
    }
    @Test
    public void compareNumbers() {
        Assert.assertEquals(-1, ObjectUtils.compare(1, 1.1));
        Assert.assertEquals(-1, ObjectUtils.compare(1, 1.0000000000001));
        Assert.assertEquals(0, ObjectUtils.compare(1, 1.0));
        Assert.assertEquals(1, ObjectUtils.compare(1.1, 1));
        Assert.assertEquals(1, ObjectUtils.compare(1.00000000000001, 1));
    }
}
