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

package org.jtwig.acceptance.functions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.jtwig.AbstractJtwigTest;
import static org.jtwig.util.SyntacticSugar.then;
import org.junit.Test;

public class NumberFunctionsTest extends AbstractJtwigTest {
    @Test
    public void testDefaultBehaviorWithoutSeparators() throws Exception {
        withResource("{{ number_format(100000.1) }}");
        then(theResult(), is(equalTo("100000.1")));
    }

    @Test
    public void testWithTwoDecimalDigits() throws Exception {
        withResource("{{ number_format(100000.1, 2) }}");
        then(theResult(), is(equalTo("100000.10")));
    }
    @Test
    public void testWithCommaSeparatingDecimal() throws Exception {
        withResource("{{ number_format(100000.1, 2, ',') }}");
        then(theResult(), is(equalTo("100000,10")));
    }
    @Test
    public void testWithThousandsSeparator() throws Exception {
        withResource("{{ number_format(100000.1, 2, ',', ' ') }}");
        then(theResult(), is(equalTo("100 000,10")));
    }
    @Test
    public void rangeTest() throws Exception {
        withResource("{{ range(1,3) }}");
        then(theResult(), is(equalTo("[1, 2, 3]")));
    }
    @Test
    public void rangeStepTest() throws Exception {
        withResource("{{ range(1, 3, 2) }}");
        then(theResult(), is(equalTo("[1, 3]")));
    }
    @Test
    public void rangeCharTest() throws Exception {
        withResource("{{ range('a', 'c') }}");
        then(theResult(), is(equalTo("[a, b, c]")));
    }
    @Test
    public void rangeStringTest() throws Exception {
        withResource("{{ range(\"AA\", \"BZ\") }}");
        then(theResult(), is(equalTo("[A, B]")));
    }
}
