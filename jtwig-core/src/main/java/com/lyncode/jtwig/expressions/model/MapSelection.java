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

package com.lyncode.jtwig.expressions.model;

import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.types.Undefined;

import java.util.Map;

import static com.lyncode.jtwig.types.Undefined.UNDEFINED;

public class MapSelection extends AbstractCompilableExpression {
    private final Variable variable;
    private final CompilableExpression key;

    public MapSelection(JtwigPosition position, Variable variable, CompilableExpression key) {
        super(position);
        this.variable = variable;
        this.key = key;
    }

    @Override
    public Expression compile(CompileContext context) throws CompileException {
        return new Compiled(position(), variable.compile(context), key.compile(context));
    }

    private static class Compiled implements Expression {
        private final JtwigPosition position;
        private final Expression variable;
        private final Expression key;

        private Compiled(JtwigPosition position, Expression variable, Expression key) {
            this.position = position;
            this.variable = variable;
            this.key = key;
        }


        @Override
        public Object calculate(RenderContext context) throws CalculateException {
            Object variableValue = variable.calculate(context);
            Object keyValue = key.calculate(context);

            if (keyValue instanceof Undefined)
                throw new CalculateException(position + ": Given key is undefined");

            if (variableValue instanceof Map) {
                Map map = (Map) variableValue;
                if (map.containsKey(keyValue))
                    return map.get(keyValue);
                else return UNDEFINED;
            } else
                throw new CalculateException(position + ": Unable to retrieve " + keyValue + " from " + variableValue);
        }
    }
}
