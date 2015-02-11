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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import org.jtwig.AbstractJtwigTest;
import org.junit.Test;

public class EmbedTest extends AbstractJtwigTest {
    @Test
    public void emptyEmbed() throws Exception {
        withResource(classpathResource("templates/embed/empty.twig"));
        assertThat(theResult(), containsString("1/1"));
    }

    @Test
    public void partialOverrideEmbed() throws Exception {
        withResource(classpathResource("templates/embed/partialOverride.twig"));
        assertThat(theResult(), containsString("1/2"));
    }

    @Test
    public void fullOverrideEmbed() throws Exception {
        withResource(classpathResource("templates/embed/fullOverride.twig"));
        assertThat(theResult(), containsString("2/2"));
    }

    @Test
    public void nestedOverrideEmbed() throws Exception {
        withResource(classpathResource("templates/embed/nestedOverride.twig"));
        assertThat(theResult(), containsString("1/(4/2)"));
    }
}
