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

package com.lyncode.jtwig.parser.model;

import com.lyncode.jtwig.resource.JtwigResource;

public class JtwigPosition {
    private JtwigResource resource;
    private int row;
    private int column;

    public JtwigPosition(JtwigResource resource, int row, int column) {
        this.resource = resource;
        this.row = row;
        this.column = column;
    }

    public JtwigResource getResource() {
        return resource;
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
