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
import com.lyncode.jtwig.tree.api.Element;
import com.lyncode.jtwig.tree.api.Expression;

public class Variable implements Element, Expression {
    private String identifier;

    public Variable(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String toString () {
        return identifier;
    }

    @Override
    public Object calculate(JtwigContext context) {
        return context.map(this.identifier);
    }
}
