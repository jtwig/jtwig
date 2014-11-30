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

package org.jtwig.configuration;

import org.jtwig.compile.config.CompileConfiguration;
import org.jtwig.functions.repository.api.FunctionRepository;
import org.jtwig.parser.config.ParserConfiguration;
import org.jtwig.render.config.RenderConfiguration;

public class JtwigConfiguration {
    private final ParserConfiguration parserConfiguration;
    private final CompileConfiguration compileConfiguration;
    private final RenderConfiguration renderConfiguration;

    public JtwigConfiguration() {
        parserConfiguration = new ParserConfiguration();
        compileConfiguration = new CompileConfiguration();
        renderConfiguration = new RenderConfiguration();
    }
    public JtwigConfiguration(FunctionRepository functionRepository) {
        parserConfiguration = new ParserConfiguration();
        compileConfiguration = new CompileConfiguration();
        renderConfiguration = new RenderConfiguration(functionRepository);
    }

    public RenderConfiguration render() { return renderConfiguration; }
    public CompileConfiguration compile () { return compileConfiguration; }
    public ParserConfiguration parse() { return parserConfiguration; }
}
