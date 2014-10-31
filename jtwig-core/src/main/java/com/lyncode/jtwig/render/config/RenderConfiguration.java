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

package com.lyncode.jtwig.render.config;

import com.google.common.base.Function;
import com.lyncode.jtwig.functions.config.JsonConfiguration;
import com.lyncode.jtwig.functions.repository.impl.MapFunctionRepository;

import java.nio.charset.Charset;

public class RenderConfiguration {
    private boolean strictMode = false;
    private boolean logNonStrictMode = true;
    private Charset charset = Charset.forName("UTF-8");
    private final JsonConfiguration jsonConfiguration;
    private final MapFunctionRepository functionRepository;

    public RenderConfiguration() {
        this.jsonConfiguration = new JsonConfiguration();
        this.functionRepository = new MapFunctionRepository(jsonConfiguration);
    }

    public boolean strictMode() {
        return strictMode;
    }

    public RenderConfiguration strictMode(boolean value) {
        this.strictMode = value;
        return this;
    }

    public boolean logNonStrictMode() {
        return logNonStrictMode;
    }

    public RenderConfiguration logNonStrictMode(boolean logNonStrictMode) {
        this.logNonStrictMode = logNonStrictMode;
        return this;
    }
    
    public Charset charset() {
        return charset;
    }
    
    public RenderConfiguration charset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public MapFunctionRepository functionRepository() {
        return functionRepository;
    }
    public RenderConfiguration jsonMapper(Function<Object, String> mapper) {
        this.jsonConfiguration.jsonMapper(mapper);
        return this;
    }
}
