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

package org.jtwig.acceptance.extension.core.tokenparsers;

import org.jtwig.JtwigModelMap;
import org.jtwig.JtwigTemplate;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

public class EmbedTest {
    @Test
    public void emptyEmbed() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate.classpathTemplate("templates/embed/empty.twig")
            .render(model);

        assertThat(result, containsString("1/1"));
    }

    @Test
    public void partialOverrideEmbed() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate.classpathTemplate("templates/embed/partialOverride.twig")
            .render(model);

        assertThat(result, containsString("1/2"));
    }

    @Test
    public void fullOverrideEmbed() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate.classpathTemplate("templates/embed/fullOverride.twig")
            .render(model);

        assertThat(result, containsString("2/2"));
    }

    @Test
    public void nestedOverrideEmbed() throws Exception {
        JtwigModelMap model = new JtwigModelMap();

        String result = JtwigTemplate.classpathTemplate("templates/embed/nestedOverride.twig")
            .render(model);

        assertThat(result, containsString("1/(4/2)"));
    }
}
