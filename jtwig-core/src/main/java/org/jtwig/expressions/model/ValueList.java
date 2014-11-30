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

import org.jtwig.compile.CompileContext;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ParseBypassException;
import org.jtwig.exception.ParseException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;

import java.util.ArrayList;
import java.util.List;

public class ValueList extends AbstractCompilableExpression {
    public static ValueList create(JtwigPosition position, Constant init, Constant end) {
        ValueList valueList = new ValueList(position);
        if (init.isInstanceOf(Integer.class)) {
            if (!end.isInstanceOf(Integer.class))
                throw new ParseBypassException(new ParseException(position + ": Expected an integer for the end of the comprehension list"));
            int initValue = (int) init.as(Integer.class);
            int endValue = (int) end.as(Integer.class);

            if (initValue < endValue) {
                for (int i = initValue; i <= endValue; i++)
                    valueList.add(new Constant<>(i));
            } else {
                for (int i = initValue; i >= endValue; i--)
                    valueList.add(new Constant<>(i));
            }

        } else if (init.isInstanceOf(Character.class)) {
            if (!end.isInstanceOf(Character.class))
                throw new ParseBypassException(new ParseException(position + ": Expected a character for the end of the comprehension list"));

            char initValue = (char) init.as(Character.class);
            char endValue = (char) end.as(Character.class);

            if (initValue < endValue) {
                for (char i = initValue; i <= endValue; i++)
                    valueList.add(new Constant<>(i));
            } else {
                for (char i = initValue; i >= endValue; i--)
                    valueList.add(new Constant<>(i));
            }
        } else
            throw new ParseBypassException(new ParseException(position + ": Only integers and characters are allowed in comprehension lists"));
        return valueList;
    }

    private final List<CompilableExpression> values = new ArrayList<>();


    public ValueList(JtwigPosition position) {
        super(position);
    }


    public ValueList add(CompilableExpression element) {
        this.values.add(element);
        return this;
    }

    @Override
    public Expression compile(CompileContext context) throws CompileException {
        List<Expression> expressions = new ArrayList<>();
        for (CompilableExpression value : values)
            expressions.add(value.compile(context));
        return new Compiled(expressions);
    }

    private static class Compiled implements Expression {
        private final List<Expression> expressions;

        private Compiled(List<Expression> expressions) {
            this.expressions = expressions;
        }

        @Override
        public Object calculate(RenderContext context) throws CalculateException {
            List<Object> result = new ArrayList<>();
            for (Expression obj : expressions)
                result.add(obj.calculate(context));
            return result;
        }
    }


}
