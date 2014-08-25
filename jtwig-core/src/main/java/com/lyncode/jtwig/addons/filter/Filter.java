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

import com.lyncode.jtwig.addons.AddonModel;
import com.lyncode.jtwig.compile.CompileContext;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CalculateException;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.expressions.api.CompilableExpression;
import com.lyncode.jtwig.expressions.api.Expression;
import com.lyncode.jtwig.expressions.model.Constant;
import com.lyncode.jtwig.expressions.model.FunctionElement;
import com.lyncode.jtwig.expressions.model.OperationBinary;
import com.lyncode.jtwig.expressions.model.Variable;
import com.lyncode.jtwig.render.RenderContext;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Filter extends AddonModel {
    private CompilableExpression expression;

    public Filter(CompilableExpression expression) {
        this.expression = expression;
    }

    @Override
    public Renderable compile(CompileContext context) throws CompileException {
        return new Compiled(super.compile(context), expression.compile(context));
    }

    private static class Compiled implements Renderable {
        private final Renderable content;
        private Expression expression;

        private Compiled(Renderable content, Expression expression) {
            this.content = content;
            this.expression = expression;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            content.render(context.newRenderContext(outputStream));

            FunctionElement.Compiled compiledFunction;
            if (expression instanceof OperationBinary.Compiled) {
                Pair<Expression, OperationBinary.Compiled> leftMostOperand = getLeftMostOperand((OperationBinary.Compiled) expression);
                compiledFunction = toFunction(leftMostOperand.getLeft());
                leftMostOperand.getRight().left(compiledFunction);
            } else {
                compiledFunction = toFunction(expression);
                expression = compiledFunction;
            }

            compiledFunction.addFirstArgument(new Constant.Compiled(outputStream.toString()));

            try {
                context.write(String.valueOf(expression.calculate(context)).getBytes());
            } catch (IOException | CalculateException e) {
                throw new RenderException(e);
            }
        }

        private FunctionElement.Compiled toFunction(Expression expression) {
            if (expression instanceof Variable.Compiled)
                return ((Variable.Compiled) expression).toFunction();
            else
                return (FunctionElement.Compiled) expression;
        }

        private Pair<Expression, OperationBinary.Compiled> getLeftMostOperand(OperationBinary.Compiled binary) {
            Expression left = binary.left();
            if (left instanceof OperationBinary.Compiled)
                return getLeftMostOperand((OperationBinary.Compiled) left);
            return new ImmutablePair<>(left, binary);
        }
    }
}
