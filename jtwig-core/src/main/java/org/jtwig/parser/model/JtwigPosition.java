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

import org.jtwig.content.model.Template;
import org.jtwig.resource.JtwigResource;

public class JtwigPosition {
    private JtwigResource resource;
    private Template template;
    private int row;
    private int column;

    public JtwigPosition(JtwigResource resource, int row, int column) {
        this.resource = resource;
        this.row = row;
        this.column = column;
    }
    public JtwigPosition(JtwigResource resource, Template template, int row, int column) {
        this(resource, row, column);
        this.template = template;
    }

    public JtwigResource getResource() {
        return resource;
    }
    
    /**
     * The template is only available to elements that were built using
     * JtwigContentParser, such as Tags. Expressions and other elements built by
     * other parsers do not have access to the template in which they were
     * created.
     * @return 
     */
    public Template getTemplate() {
        return template;
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
