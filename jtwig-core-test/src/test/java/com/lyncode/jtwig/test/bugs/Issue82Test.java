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

package com.lyncode.jtwig.test.bugs;

import com.lyncode.jtwig.test.AbstractJtwigTest;
import org.junit.Test;

import static com.lyncode.jtwig.SyntacticSugar.then;
import static com.lyncode.jtwig.SyntacticSugar.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class Issue82Test extends AbstractJtwigTest {
    @Test
    public void issue82() throws Exception {
        when(jtwigRenders(template("{{ notice }}")));
        then(theRenderedTemplate(), is(equalTo("undefined")));
    }
}
