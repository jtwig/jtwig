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

package org.jtwig.compile;

import org.jtwig.Environment;
import org.jtwig.cache.TemplateCache;
import org.jtwig.content.model.compilable.Sequence;

import org.jtwig.loader.Loader;

public class CompileContext {
    private Loader.Resource resource;
    private final Environment env;
    private Sequence parent;

    public CompileContext(Loader.Resource resource, Environment env) {
        this.resource = resource;
        this.env = env;
        this.parent = null;
    }

    public CompileContext withParent(Sequence element) {
        this.parent = element;
        return this;
    }

    public boolean hasParent () {
        return parent != null;
    }

    public Sequence parent () {
        return parent;
    }
    
    public Environment environment() {
        return env;
    }
    
    public TemplateCache cache() {
        return env.getConfiguration().getTemplateCache();
    }

    public CompileContext clone() {
        CompileContext compileContext = new CompileContext(resource, env);
        compileContext.withParent(parent);
        return compileContext;
    }

    public CompileContext withResource(Loader.Resource retrieve) {
        this.resource = retrieve;
        return this;
    }
}
