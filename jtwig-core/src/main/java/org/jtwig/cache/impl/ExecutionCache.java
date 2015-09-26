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

package org.jtwig.cache.impl;

import java.util.HashMap;
import java.util.Map;
import org.jtwig.cache.TemplateCache;
import org.jtwig.content.model.Template;

/**
 * The execution cache keeps compiled templates around for the duration of the
 * compile and render processes, and then it is discarded after the process is
 * complete.
 */
public class ExecutionCache implements TemplateCache {
    private final Map<String, Template> parsed = new HashMap<>();
    private final Map<String, Template.Compiled> compiled = new HashMap<>();
    

    @Override
    public TemplateCache addParsed(final String name, final Template template) {
        parsed.put(name, template);
        return this;
    }

    @Override
    public Template getParsed(final String name) {
        if (name == null) {
            return null;
        }
        if (parsed.containsKey(name)) {
            return parsed.get(name);
        }
        return null;
    }

    @Override
    public Template removeParsed(final String name) {
        if (name == null) {
            return null;
        }
        if (parsed.containsKey(name)) {
            return parsed.remove(name);
        }
        return null;
    }
    
    @Override
    public ExecutionCache addCompiled(final String name,
            final Template.Compiled template) {
        this.compiled.put(name, template);
        return this;
    }
    
    @Override
    public Template.Compiled getCompiled(final String name) {
        if (name == null) {
            return null;
        }
        if (compiled.containsKey(name)) {
            return compiled.get(name);
        }
        return null;
    }

    @Override
    public Template.Compiled removeCompiled(final String name) {
        if (name == null) {
            return null;
        }
        if (compiled.containsKey(name)) {
            return compiled.remove(name);
        }
        return null;
    }
}