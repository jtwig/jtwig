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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import org.jtwig.AbstractJtwigTest;
import org.jtwig.content.model.Template;
import static org.jtwig.util.SyntacticSugar.then;
import org.junit.Test;

public class MacroTest extends AbstractJtwigTest {
    @Test
    public void ensureMacrosAreAddedToTemplate() throws Exception {
        withResource(classpathResource("templates/acceptance/macro/macro.twig"));
        Template.CompiledTemplate ct = theEnvironment().compile(resource);
        then(ct.macros().size(), is(equalTo(2)));
    }
    
    @Test
    public void ensureLastMacroDefinedWithSameNameIsUsed() throws Exception {
        withResource(classpathResource("templates/acceptance/macro/overloading.twig"));
        Template.CompiledTemplate t = theEnvironment().compile(resource);
        then(t.macros().size(), is(equalTo(1)));
        then(t.macros().entrySet().iterator().next().getValue().arguments().size(), is(equalTo(1)));
    }
}