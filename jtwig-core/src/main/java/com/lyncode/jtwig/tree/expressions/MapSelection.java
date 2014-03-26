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
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.functions.types.Undefined;
import com.lyncode.jtwig.tree.api.Expression;

import java.util.Map;

import static com.lyncode.jtwig.functions.types.Undefined.UNDEFINED;

public class MapSelection implements Expression {
    private Variable variable;
    private Expression key;

    public MapSelection(Variable variable, Expression key) {
        this.variable = variable;
        this.key = key;
    }

    @Override
    public Object calculate(JtwigContext context) throws CalculateException {
        Object resolved = variable.calculate(context);
        Object keyValue = this.key.calculate(context);

        if (keyValue instanceof Undefined)
            throw new CalculateException("Given key is undefined");

        if (resolved instanceof Map) {
            Map map = (Map) resolved;
            if (map.containsKey(keyValue))
                return map.get(keyValue);
            else return UNDEFINED;
        }
        else
            throw new CalculateException("Invalid input. Should be given a Map but a "+resolved.getClass().getName()+" was given.");
    }
}
