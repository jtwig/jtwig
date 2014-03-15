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

package com.lyncode.jtwig.functions.internal.list;

import com.lyncode.jtwig.functions.exceptions.FunctionException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class SortTest {

    private Sort underTest = new Sort();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testForList() throws Exception {

        List<String> toSort = asList("a", "c", "d", "1", "0", "z", "w");
        List<String> target = asList("0", "1", "a", "c", "d", "w", "z");

        assertEquals(target, underTest.execute(toSort));
    }

    @Test
    public void shouldThrowExceptionIfInvalidArgumentsAreGiven () throws FunctionException {
        exception.expect(FunctionException.class);
        exception.expectMessage("First argument must be an instance of List");
        underTest.execute("");
    }

}
