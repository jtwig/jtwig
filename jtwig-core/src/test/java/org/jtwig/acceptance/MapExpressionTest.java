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
import static org.hamcrest.core.IsEqual.equalTo;
import org.jtwig.AbstractJtwigTest;
import static org.jtwig.util.SyntacticSugar.then;
import org.junit.Test;

public class MapExpressionTest extends AbstractJtwigTest {
    @Test
    public void mapWithStringLiteralsAsKey () throws Exception {
        withResource("{% set a = { 'test two': 'details' } %}{{ a['test two'] }}");
        then(theResult(), is(equalTo("details")));
    }

    @Test
    public void ifWithEmptyMapShouldBeTheSameAsFalse () throws Exception {
        model.withModelAttribute("map", Collections.EMPTY_MAP);
        withResource("{% if (map) %}not empty{% else %}empty{% endif %}");
        assertThat(theResult(), is("empty"));
    }

    @Test
    public void ifNoKeyInMapTryMethods () throws Exception {
        model.withModelAttribute("map", Collections.EMPTY_MAP);
        withResource("{{ map.size }}");
        assertThat(theResult(), is("0"));
    }
    @Test
    public void methodsAndFieldsShouldPrevail () throws Exception {
        model.withModelAttribute("map", Collections.singletonMap("size", "Hello!"));
        withResource("{{ map.size }}");
        assertThat(theResult(), is("1"));
    }
}
