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

package org.jtwig.parser.model;

import org.jtwig.compile.CompileContext;
import org.jtwig.content.model.Template;
import org.jtwig.loader.Loader;
import org.jtwig.render.RenderContext;
import org.slf4j.LoggerFactory;

public class JtwigPosition {
    private final Loader.Resource resource;
    private final int row;
    private final int column;

    public JtwigPosition(Loader.Resource resource, int row, int column) {
//        if (resource == null) {
//            LoggerFactory.getLogger(JtwigPosition.class).debug("Created jtwig position without resource "+hashCode(), new Exception());
//        } else if (resource.toString().isEmpty()) {
//            LoggerFactory.getLogger(JtwigPosition.class).debug("Created jtwig position with empty resource path "+hashCode(), new Exception());
//        }
        this.resource = resource;
        this.row = row;
        this.column = column;
    }

    public Loader.Resource getResource() {
        return resource;
    }
    
    public Template getTemplate(CompileContext compileContext) {
        return compileContext.cache().getParsed(resource.getCacheKey());
    }
    
    public Template.Compiled getCompiledTemplate(CompileContext compileContext) {
        return compileContext.cache().getCompiled(resource.getCacheKey());
    }
    
    public Template getTemplate(RenderContext renderContext) {
        return renderContext.cache().getParsed(resource.getCacheKey());
    }
    
    public Template.Compiled getCompiledTemplate(RenderContext renderContext) {
        return renderContext.cache().getCompiled(resource.getCacheKey());
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return resource + " -> Line "+row+", column "+column;
    }
}
