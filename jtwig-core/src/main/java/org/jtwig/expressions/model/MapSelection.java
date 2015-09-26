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

package org.jtwig.expressions.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jtwig.compile.CompileContext;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.jtwig.types.Undefined;

import java.util.Map;

import static org.jtwig.types.Undefined.UNDEFINED;
import org.jtwig.util.TypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapSelection extends AbstractCompilableExpression {
    private final CompilableExpression source;
    private final CompilableExpression key;

    public MapSelection(JtwigPosition position, CompilableExpression source, CompilableExpression key) {
        super(position);
        this.source = source;
        this.key = key;
    }

    @Override
    public Expression compile(CompileContext context) throws CompileException {
        return new Compiled(position(), source.compile(context), key.compile(context));
    }

    private static class Compiled implements Expression {
        private static final Logger LOGGER = LoggerFactory.getLogger(Compiled.class);
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

            if (keyValue instanceof Undefined) {
                throw new CalculateException(position + ": Given key is undefined");
            }
            
            if (variableValue == null || variableValue instanceof Undefined) {
                error(context, variableValue, keyValue);
                return UNDEFINED;
            }

            if (variableValue instanceof Map) {
                Map map = (Map) variableValue;
                if (map.containsKey(keyValue)) {
                    return map.get(keyValue);
                }
                error(context, variableValue, keyValue);
                return UNDEFINED;
            }
            if (!(variableValue instanceof Collection)) {
                error(context, variableValue, keyValue);
                return UNDEFINED;
            }
            int idx = TypeUtil.toLong(keyValue).intValue();
            if (((Collection)variableValue).size() <= idx) {
                error(context, variableValue, keyValue);
                return UNDEFINED;
            }
            if (!(variableValue instanceof List)) {
                variableValue = new ArrayList<>((Collection)variableValue);
            }
            return ((List)variableValue).get(idx);
        }
        
        private void error(RenderContext context, Object value, Object key) throws CalculateException {
            if (context.environment().getConfiguration().isStrictMode()) {
                throw new CalculateException(position + ": Unable to retrieve "+key+" from "+value);
            } else if (context.environment().getConfiguration().isLogNonStrictMode()) {
                LOGGER.warn(position + ": Unable to retrieve "+key+" from "+value);
            }
        }
    }
}
