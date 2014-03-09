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

package com.lyncode.jtwig.tree.content;

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.tree.api.Expression;
import com.lyncode.jtwig.tree.expressions.Variable;
import com.lyncode.jtwig.tree.helper.RenderStream;

import java.util.Map;

public class ForPairLoop extends ForLoop {
    private Variable value;

    public ForPairLoop(Variable key, Variable value, Expression map) {
        super(key, map);
        this.value = value;
    }

    @Override
    public boolean render(RenderStream renderStream, JtwigContext context) throws RenderException {
        try {
            Object resolved = expression.calculate(context);

            if (!(resolved instanceof Map)) {
                throw new RenderException("Expecting a map as parameter for the loop but " + expression + " was given");
            }

            Map map = (Map) resolved;
            Loop loop = new Loop(map.size());
            context.set("loop", loop);
            int index = 0;
            for (Object key : map.keySet()) {
                loop.update(index++);
                context.set(variable.getIdentifier(), key);
                context.set(value.getIdentifier(), map.get(key));
                content.render(renderStream, context);
            }
            return true;
        } catch (CalculateException e) {
            throw new RenderException(e);
        }
    }

    public String toString() {
        return "For each element of " + super.expression + " render " + content;
    }
}
