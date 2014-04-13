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

package com.lyncode.jtwig.acceptance;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.JtwigTemplate;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TernaryOperatorTest extends AbstractJtwigTest {
    @Test
    public void testTrue() throws Exception {
        JtwigTemplate template = new JtwigTemplate("{{ true ? 1 : 2 }}");
        String result = template.output(new JtwigContext());
        assertThat(result, is("1"));
    }
    @Test
     public void testFalse() throws Exception {
        JtwigTemplate template = new JtwigTemplate("{{ false ? 1 : 2 }}");
        String result = template.output(new JtwigContext());
        assertThat(result, is("2"));
    }
}
