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

package org.jtwig.acceptance.issues;

import org.jtwig.acceptance.AbstractJtwigTest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.jtwig.util.SyntacticSugar.then;
import static org.jtwig.util.SyntacticSugar.when;

public class Issue97Test extends AbstractJtwigTest {
    @Test
    public void testDecimalResult() throws Exception {
        when(jtwigRenders(template("{{ 1 / 10 }}")));
        then(theRenderedTemplate(), is(equalTo("0.1")));
    }
    @Test
    public void testIntegerDivision() throws Exception {
        when(jtwigRenders(template("{{ 1 // 10 }}")));
        then(theRenderedTemplate(), is(equalTo("0")));
    }
}
