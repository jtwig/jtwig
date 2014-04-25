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

import java.util.LinkedHashMap;
import java.util.Map;

public class ValueMap extends AbstractCompilableExpression {
    private Map<String, CompilableExpression> map = new LinkedHashMap<>();

    public ValueMap(JtwigPosition position) {
        super(position);
    }

    public ValueMap add(String key, CompilableExpression element) {
        map.put(key, element);
        return this;
    }

    @Override
    public Expression compile(CompileContext context) throws CompileException {
        Map<String, Expression> result = new LinkedHashMap<>();
        for (Map.Entry<String, CompilableExpression> entry : map.entrySet())
            result.put(entry.getKey(), entry.getValue().compile(context));
        return new Compiled(result);
    }

    private static class Compiled implements Expression {
        private final Map<String, Expression> expressions;

        private Compiled(Map<String, Expression> expressions) {
            this.expressions = expressions;
        }

        @Override
        public Object calculate(RenderContext context) throws CalculateException {
            Map<String, Object> result = new LinkedHashMap<>();
            for (Map.Entry<String, Expression> entry : expressions.entrySet())
                result.put(entry.getKey(), entry.getValue().calculate(context));
            return result;
        }
    }
}
