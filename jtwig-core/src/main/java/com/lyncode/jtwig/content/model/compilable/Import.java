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

package com.lyncode.jtwig.content.model.compilable;

import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Compilable;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseBypassException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;
import java.util.HashMap;
import java.util.Map;

public class Import extends AbstractElement {
    private final JtwigPosition position;
    private CompilableExpression from;
    private Map<CompilableExpression, CompilableExpression> imports = new HashMap<>();
    
    public Import(final JtwigPosition position) {
        this.position = position;
    }
    
    public Import from(final CompilableExpression from) {
        this.from = from;
        return this;
    }
    public CompilableExpression from() {
        return from;
    }
    public Import add(final Definition def) {
        if (from == null && imports.size() > 0) {
            throw new ParseBypassException(new ParseException("Cannot have more than one import definition in an import statement."));
        }
        if (from == null && def.as == null) {
            throw new ParseBypassException(new ParseException("Must specify 'as'"));
        }
        
        if (def.as == null) {
            imports.put(def.source(), def.source());
        } else {
            imports.put(def.source, def.as());
        }
        return this;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        return new Renderable() {
            @Override
            public void render(RenderContext context) throws RenderException {}
        };
    }
    
    public static class Definition implements Compilable {
        private final CompilableExpression source;
        private CompilableExpression as;
        
        public Definition(final CompilableExpression source) {
            this.source = source;
        }
        
        public Definition as(final CompilableExpression as) {
            this.as = as;
            return this;
        }
        
        public CompilableExpression source() {
            return source;
        }
        
        public CompilableExpression as() {
            return as;
        }

        @Override
        public Renderable compile(CompileContext context) throws CompileException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
    
}