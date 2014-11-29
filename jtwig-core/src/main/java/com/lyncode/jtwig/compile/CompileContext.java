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

package com.lyncode.jtwig.compile;

import com.lyncode.jtwig.compile.config.CompileConfiguration;
import com.lyncode.jtwig.content.api.Compilable;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.content.model.compilable.Macro;
import com.lyncode.jtwig.content.model.compilable.Sequence;
import com.lyncode.jtwig.content.model.renderable.Replacement;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.ResourceException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.resource.JtwigResource;

import java.util.HashMap;
import java.util.Map;

public class CompileContext {
    private JtwigResource resource;
    private final JtwigParser parser;
    private final CompileConfiguration configuration;
    private Sequence parent;
    private Map<String, Renderable> replacements = new HashMap<>();
    private Map<JtwigResource, Map<String, Macro.Compiled>> macros = new HashMap<>();

    public CompileContext(JtwigResource resource, JtwigParser parser, CompileConfiguration configuration) {
        this.resource = resource;
        this.parser = parser;
        this.configuration = configuration;
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

    public CompileContext withReplacement(String name, Renderable replacement) {
        if (replacements.containsKey(name)) {
            // already contains, nested replacement
            replacements.put(name, new Replacement(replacements.get(name), replacement));
        } else {
            replacements.put(name, replacement);
        }
        return this;
    }

    public CompileContext withReplacement(Map<String, Renderable> replacements) {
        this.replacements.putAll(replacements);
        return this;
    }

    public boolean hasReplacement(String name) {
        return replacements.containsKey(name);
    }

    public Renderable replacement(String name) {
        return replacements.get(name);
    }
    
    public CompileContext withMacro(JtwigResource resource, String name, Macro.Compiled macro) {
        if (!macros.containsKey(resource)) {
            macros.put(resource, new HashMap<String, Macro.Compiled>());
        }
        macros.get(resource).put(name, macro);
        return this;
    }
    
    public Map<JtwigResource, Map<String, Macro.Compiled>> macros() {
        return macros;
    }
    
    public Map<String, Macro.Compiled> macros(JtwigResource resource) {
        if (macros.containsKey(resource)) {
            return macros.get(resource);
        }
        return null;
    }

    public JtwigResource retrieve(String relativePath) throws ResourceException {
        return resource.resolve(relativePath);
    }

    public Compilable parse (JtwigResource resource) throws ParseException {
        return parser.parse(resource);
    }

    public CompileContext clone() {
        CompileContext compileContext = new CompileContext(resource, parser, configuration);
        compileContext
                .withParent(parent)
                .withReplacement(replacements);
        compileContext.macros = this.macros;
        return compileContext;
    }

    public CompileContext withResource(JtwigResource retrieve) {
        this.resource = retrieve;
        return this;
    }
}
