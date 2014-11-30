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

import org.jtwig.compile.CompileContext;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.parser.JtwigParser;
import org.jtwig.resource.JtwigResource;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.jtwig.util.SyntacticSugar.then;

public class MacroTest extends AbstractJtwigTest {
    @Test
    public void ensureMacrosAreAddedToContext() throws Exception {
        JtwigResource resource = templateResource("templates/acceptance/macro/macro.twig");
        JtwigConfiguration configuration = new JtwigConfiguration();
        JtwigParser parser = new JtwigParser(configuration.parse());
        CompileContext ctx = new CompileContext(resource, parser, configuration.compile());
        parser.parse(resource)
                .compile(ctx);
        then(ctx.macros().size(), is(equalTo(1)));
        then(ctx.macros(resource).size(), is(equalTo(2)));
    }
    
    @Test
    public void ensureLastMacroDefinedWithSameNameIsUsed() throws Exception {
        JtwigResource resource = templateResource("templates/acceptance/macro/overloading.twig");
        JtwigConfiguration configuration = new JtwigConfiguration();
        JtwigParser parser = new JtwigParser(configuration.parse());
        CompileContext ctx = new CompileContext(resource, parser, configuration.compile());
        parser.parse(resource)
                .compile(ctx);
        then(ctx.macros().size(), is(equalTo(1)));
        then(ctx.macros(resource).size(), is(equalTo(1)));
        then(ctx.macros(resource).entrySet().iterator().next().getValue().arguments().size(), is(equalTo(1)));
    }
}
