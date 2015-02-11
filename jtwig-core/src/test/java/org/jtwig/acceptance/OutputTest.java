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

import java.util.Collections;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.jtwig.AbstractJtwigTest;
import org.junit.Test;

public class OutputTest extends AbstractJtwigTest {
    @Test
    public void shouldAllowConcatenationOfDistinctElements () throws Exception {
        model.withModelAttribute("list", Collections.EMPTY_LIST);
        withResource("{{ concat ('1', list.size ,'3') }}");
        assertThat(theResult(), is("103"));
    }

    @Test
    public void shouldAllowFilters () throws Exception {
        withResource("{{ ['1', '2' ,'3'] | join(',') }}");
        assertThat(theResult(), is("1,2,3"));
    }
}
