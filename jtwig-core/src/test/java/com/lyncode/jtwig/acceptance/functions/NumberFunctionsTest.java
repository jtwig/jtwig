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

package com.lyncode.jtwig.acceptance.functions;

import com.lyncode.jtwig.acceptance.AbstractJtwigTest;
import org.junit.Test;

import static com.lyncode.jtwig.util.SyntacticSugar.then;
import static com.lyncode.jtwig.util.SyntacticSugar.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class NumberFunctionsTest extends AbstractJtwigTest {
    @Test
    public void testDefaultBehaviorWithoutSeparators() throws Exception {
        when(jtwigRenders(template("{{ number_format(100000.1) }}")));
        then(theRenderedTemplate(), is(equalTo("100000.1")));
    }

    @Test
    public void testWithTwoDecimalDigits() throws Exception {
        when(jtwigRenders(template("{{ number_format(100000.1, 2) }}")));
        then(theRenderedTemplate(), is(equalTo("100000.10")));
    }
    @Test
    public void testWithCommaSeparatingDecimal() throws Exception {
        when(jtwigRenders(template("{{ number_format(100000.1, 2, ',') }}")));
        then(theRenderedTemplate(), is(equalTo("100000,10")));
    }
    @Test
    public void testWithThousandsSeparator() throws Exception {
        when(jtwigRenders(template("{{ number_format(100000.1, 2, ',', ' ') }}")));
        then(theRenderedTemplate(), is(equalTo("100 000,10")));
    }
    @Test
    public void rangeTest() throws Exception {
        when(jtwigRenders(template("{{ range(1,3) }}")));
        then(theRenderedTemplate(), is(equalTo("[1, 2, 3]")));
    }
    @Test
    public void rangeStepTest() throws Exception {
        when(jtwigRenders(template("{{ range(1, 3, 2) }}")));
        then(theRenderedTemplate(), is(equalTo("[1, 3]")));
    }
    @Test
    public void rangeCharTest() throws Exception {
        when(jtwigRenders(template("{{ range('a', 'c') }}")));
        then(theRenderedTemplate(), is(equalTo("[a, b, c]")));
    }
    @Test
    public void rangeStringTest() throws Exception {
        when(jtwigRenders(template("{{ range(\"AA\", \"BZ\") }}")));
        then(theRenderedTemplate(), is(equalTo("[A, B]")));
    }
}
