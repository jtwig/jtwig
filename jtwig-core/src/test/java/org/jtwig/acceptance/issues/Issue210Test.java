/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.acceptance.issues;

import org.jtwig.AbstractJtwigTest;
import org.junit.Test;

import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.jtwig.util.SyntacticSugar.*;

/**
 * 
 */
public class Issue210Test extends AbstractJtwigTest {
    @Test
    public void testNonUTFEncoding() throws Exception {
        theEnvironment().setCharset(Charset.forName("ISO-8859-1"));
        given(theModel().withModelAttribute("text", "tête de bou  간편한 설치 및 사용"));
        withResource("{{ text }}");
        then(theResult(), is(equalTo("t�te de bou  ??? ?? ? ??")));
    }
    @Test
    public void testUTFEncoding() throws Exception {
        given(theModel().withModelAttribute("text", "tête de bou  간편한 설치 및 사용"));
        withResource("{{ text }}");
        then(theResult(), is(equalTo("tête de bou  간편한 설치 및 사용")));
    }
}
