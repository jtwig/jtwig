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

package com.lyncode.jtwig.addons.filter;

import com.google.common.base.Function;
import com.lyncode.jtwig.addons.AddonModel;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.expressions.model.FunctionElement;
import com.lyncode.jtwig.expressions.model.OperationBinary;
import com.lyncode.jtwig.parser.model.JtwigPosition;
import com.lyncode.jtwig.render.RenderContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Filter extends AddonModel<Filter> {
    private final JtwigPosition position;
    private CompilableExpression expression;

    public Filter(JtwigPosition position, CompilableExpression expression) {
        this.position = position;
        this.expression = expression;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        if (expression instanceof OperationBinary) {
            OperationBinary operationBinary = (OperationBinary) expression;
            operationBinary.transformFirst(toSpecialFunction());
        }
        return new Compiled(position, super.compile(context), expression.compile(context));
    }

    private Function<CompilableExpression, CompilableExpression> toSpecialFunction() {
        return new Function<CompilableExpression, CompilableExpression>() {
            @Override
            public CompilableExpression apply(CompilableExpression input) {
                return new DelegateCompilable((FunctionElement)input);
            }
        };
    }

    private static class Compiled implements Renderable {
        private final JtwigPosition position;
        private final Renderable content;
        private Expression expression;

        private Compiled(JtwigPosition position, Renderable content, Expression expression) {
            this.position = position;
            this.content = content;
            this.expression = expression;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            content.render(context.newRenderContext(outputStream));
            try {
                if (expression instanceof DelegateCalculable)
                    ((DelegateCalculable) expression).withArgument(outputStream.toString());
                else
                    getDelegateExpression(position, (FunctionElement.Compiled) expression).withArgument(outputStream.toString());
                context.write(String.valueOf(expression.calculate(context)).getBytes());
            } catch (IOException | CalculateException e) {
                throw new RenderException(e);
            }
        }

        private DelegateCalculable getDelegateExpression(JtwigPosition position, FunctionElement.Compiled function) {
            Expression firstArgument = function.getFirstArgument();
            if (firstArgument instanceof DelegateCalculable) {
                return (DelegateCalculable) firstArgument;
            } else {
                if ((firstArgument instanceof FunctionElement.Compiled) && ((FunctionElement.Compiled) firstArgument).hasArguments()) {
                    return getDelegateExpression(position, (FunctionElement.Compiled) firstArgument);
                } else throw new RuntimeException(position+": Unable to find function to replace input.");
            }
        }
    }

    private class DelegateCompilable implements CompilableExpression {
        private final FunctionElement delegate;

        public DelegateCompilable(FunctionElement delegate) {
            this.delegate = delegate;
        }

        @Override
        public Expression compile(CompileContext context) throws CompileException {
            FunctionElement.Compiled compile = (FunctionElement.Compiled) delegate.compile(context);
            return new DelegateCalculable(compile);
        }
    }

    public static class DelegateCalculable implements Expression {
        private FunctionElement.Compiled function;

        public DelegateCalculable(FunctionElement.Compiled function) {
            this.function = function;
        }

        public DelegateCalculable withArgument (final String value) {
            this.function = function.cloneAndAddArgument(valueOf(value));
            return this;
        }

        @Override
        public Object calculate(RenderContext context) throws CalculateException {
            return function.calculate(context);
        }
    }

    private static Expression valueOf(final String value) {
        return new Expression() {
            @Override
            public Object calculate(RenderContext context) throws CalculateException {
                return value;
            }
        };
    }
}
