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

package com.lyncode.jtwig.functions.internal.math;

import com.lyncode.jtwig.functions.exceptions.FunctionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JNumberFormatTest {
    private JNumberFormat underTest = new JNumberFormat();

    @Test
    public void testExecute() throws FunctionException {
        assertEquals("1,234.57", underTest.execute(1234.5678, 2, ".", ","));
    }
}
