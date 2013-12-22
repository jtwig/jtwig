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

import com.lyncode.jtwig.functions.exceptions.FunctionException;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class JoinTest {
    private Join underTest = new Join();

    @Test
    public void shouldNotIgnoreNullValues () throws FunctionException {
        String result = (String) underTest.execute(asList(null, "1", "2"), ", ");
        assertThat(result, is(", 1, 2"));
    }

    @Test
    public void shouldThrowExceptionIfInvalidArgumentsAreGiven () throws FunctionException {
        String execute = (String) underTest.execute(null, ", ");
        assertThat(execute, is(""));
    }

    @Test
    public void shouldUseEmptyStringIfSecondArgumentIsNotGiven () throws FunctionException {
        String result = (String) underTest.execute(asList(null, "1", "2"));
        assertThat(result, is("12"));
    }
}
