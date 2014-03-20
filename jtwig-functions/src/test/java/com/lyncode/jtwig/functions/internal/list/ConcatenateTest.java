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

package com.lyncode.jtwig.functions.internal.list;

import com.lyncode.jtwig.functions.exceptions.FunctionException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConcatenateTest {
    private Concatenate underTest = new Concatenate();

    @Test
    public void shouldNotIncludeNullValues () throws FunctionException {
        String result = (String) underTest.execute("Hi! ", null, "My name is Twig!");
        assertThat(result, is("Hi! My name is Twig!"));
    }

    @Test
    public void shouldGiveEmptyStringIfExecutedWithoutArguments () throws FunctionException {
        String result = (String) underTest.execute();
        assertThat(result, is(""));
    }
}
