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

package org.jtwig.acceptance;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.jtwig.AbstractJtwigTest;
import org.junit.Test;

public class ListExpressionTest extends AbstractJtwigTest {
    @Test
    public void integerListByComprehension () throws Exception {
        withResource("{{ (1..5) | join(',') }}");
        assertThat(theResult(), is("1,2,3,4,5"));
    }
    @Test
    public void integerListByComprehensionReverse () throws Exception {
        withResource("{{ (5..1) | join(',') }}");
        assertThat(theResult(), is("5,4,3,2,1"));
    }

    @Test
    public void characterListByComprehension () throws Exception {
        withResource("{{ ('a'..'c') | join }}");
        assertThat(theResult(), is("abc"));
    }
    @Test
    public void characterListByComprehensionReverse () throws Exception {
        withResource("{{ ('c'.. 'a') | join }}");
        assertThat(theResult(), is("cba"));
    }
}