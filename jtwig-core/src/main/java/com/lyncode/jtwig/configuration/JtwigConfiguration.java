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

package com.lyncode.jtwig.configuration;

import com.lyncode.jtwig.compile.config.CompileConfiguration;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.render.config.RenderConfiguration;

public class JtwigConfiguration {
    private ParserConfiguration parserConfiguration = new ParserConfiguration();
    private CompileConfiguration compileConfiguration = new CompileConfiguration();
    private RenderConfiguration renderConfiguration = new RenderConfiguration();

    public RenderConfiguration render() { return renderConfiguration; }
    public CompileConfiguration compile () { return compileConfiguration; }
    public ParserConfiguration parse() { return parserConfiguration; }
}
