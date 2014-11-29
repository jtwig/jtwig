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
import com.lyncode.jtwig.render.RenderContext;
import java.util.ArrayList;
import java.util.List;

public class ValueRange implements CompilableExpression {
    private CompilableExpression start;
    private CompilableExpression end;

    public ValueRange withStart(CompilableExpression start) {
        this.start = start;
        return this;
    }
    
    public ValueRange withEnd(CompilableExpression end) {
        this.end = end;
        return this;
    }
    
    @Override
    public Expression compile(CompileContext context) throws CompileException {
        return new Compiled(start.compile(context), end.compile(context));
    }
    
    private static class Compiled implements Expression {
        private final Expression start;
        private final Expression end;

        private Compiled(Expression start, Expression end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public Object calculate(RenderContext context) throws CalculateException {
            Object start = this.start.calculate(context);
            Object end = this.end.calculate(context);
            
            // In twig, numbers take precedence
            int startInt;
            int endInt;
            int step = 1;
            if (start instanceof Number || end instanceof Number) {
                startInt = start instanceof Number ? ((Number)start).intValue() : 0;
                endInt = end instanceof Number ? ((Number)end).intValue() : 0;
            } else {
                if (start instanceof Character) {
                    startInt = ((Character)start).charValue();
                } else if (start instanceof CharSequence) {
                    startInt = ((CharSequence)start).charAt(0);
                } else {
                    startInt = 0;
                }
                if (end instanceof Character) {
                    endInt = ((Character)end).charValue();
                } else if (end instanceof CharSequence) {
                    endInt = ((CharSequence)end).charAt(0);
                } else {
                    endInt = 0;
                }
            }
            
            // Handle negative progressions
            if (startInt > endInt) {
                step = -step;
            }

            // Build the list and convert if necessary
            List results;
            if (start instanceof Number || end instanceof Number) {
                results = new ArrayList<Number>();
                for (int i = startInt; (step > 0) ? i <= endInt : i >= endInt; i += step) {
                    results.add(i);
                }
            } else {
                results = new ArrayList<String>();
                for (int i = startInt; (step > 0) ? i <= endInt : i >= endInt; i += step) {
                    results.add(Character.toString((char)i));
                }
            }
            return results;
        }
    }
}