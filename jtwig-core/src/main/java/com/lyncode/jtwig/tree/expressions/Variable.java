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

package com.lyncode.jtwig.tree.expressions;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.parser.positioning.Position;
import com.lyncode.jtwig.tree.api.AbstractExpression;
import com.lyncode.jtwig.types.Undefined;

public class Variable extends AbstractExpression {
    private String identifier;
    private boolean emptyOnUndefined = false;

    public Variable(Position position, String identifier) {
        super(position);
        this.identifier = identifier;
    }
    public Variable(Position position, String identifier, boolean emptyOnUndefined) {
        this(position, identifier);
        this.emptyOnUndefined = emptyOnUndefined;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String toString () {
        return identifier;
    }

    @Override
    public Object calculate(JtwigContext context) {
        Object obj = context.map(this.identifier);
        if((obj == Undefined.UNDEFINED || obj == null)
                && emptyOnUndefined) {
            return "";
        }
        return obj;
    }
}
