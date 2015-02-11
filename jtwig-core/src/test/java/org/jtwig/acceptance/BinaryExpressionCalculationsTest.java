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

public class BinaryExpressionCalculationsTest extends AbstractJtwigTest {
    @Test
    public void shouldResolveToInteger () throws Exception {
        assertThat(theResultOf(theTemplate("{{ 3 ** 2 }}")), is("6"));
    }

    @Test
    public void shouldResolveToDouble () throws Exception {
        assertThat(theResultOf(theTemplate("{{ 3 * 2.0 }}")), is("6.0"));
    }

    @Test
    public void shouldResolveToBoolean () throws Exception {
        assertThat(theResultOf(theTemplate("{{ 3 and 2.0 }}")), is("1"));
    }

    @Test
    public void shouldResolveToBooleanFalse () throws Exception {
        assertThat(theResultOf(theTemplate("{{ 3 and false }}")), is("0"));
    }
}
