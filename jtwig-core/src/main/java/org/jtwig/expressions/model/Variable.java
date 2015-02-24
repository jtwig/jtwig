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
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.jtwig.types.Undefined;
import org.jtwig.util.ObjectExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static java.lang.String.format;

public class Variable extends AbstractCompilableExpression {
    private static Logger log = LoggerFactory.getLogger(Variable.class);
    private String name;

    public Variable(JtwigPosition position, String name) {
        super(position);
        this.name = name;
    }
    
    public String name() {
        return name;
    }

    @Override
    public Expression compile(CompileContext context) throws CompileException {
        return new Compiled(position(), name);
    }

    public CompilableExpression toFunction() {
        return new FunctionElement(position(), name);
    }

    public static class Compiled implements Expression {
        private final String name;
        private final JtwigPosition position;

        public Compiled(JtwigPosition position, String name) {
            this.position = position;
            this.name = name;
        }


        public FunctionElement.Compiled toFunction () {
            return new FunctionElement.Compiled(position, name, new ArrayList<Expression>());
        }

        @Override
        public Object calculate(RenderContext context) throws CalculateException {
            Object result = context.map(name);
            if (result instanceof Undefined) {
                if (context.environment().getConfiguration().isStrictMode())
                    throw new CalculateException(position + format(": Variable '%s' does not exist", name));
                else if (context.environment().getConfiguration().isLogNonStrictMode())
                    log.debug(position + format(": Variable '%s' does not exist", name));
            }
            return result;
        }

        public Object extract(ObjectExtractor extractor) throws ObjectExtractor.ExtractException {
            // if we reach this we are already sure that the extractor content is not null/undefined
            return extractor.extract(name);
        }

        public String name () {
            return name;
        }
    }
}
