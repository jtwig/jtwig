/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.extension.core.tokenparsers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Compilable;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.compilable.Content;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ParseException;
import org.jtwig.exception.RenderException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.extension.api.tokenparser.Tag;
import org.jtwig.extension.model.FilterCall;
import org.jtwig.loader.Loader;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.jtwig.render.RenderContext;
import org.parboiled.Rule;
import org.parboiled.annotations.Label;

public class FilterTag extends Tag {

    public FilterTag(Loader.Resource resource, JtwigContentParser content, JtwigBasicParser basic, JtwigExpressionParser expr, JtwigTagPropertyParser tag) {
        super(resource, content, basic, expr, tag);
    }

    @Override
    public String getKeyword() {
        return "filter";
    }

    @Override
    public Compilable model(JtwigPosition pos) {
        return new Filter(pos);
    }

    @Label("Filter tag")
    @Override
    public Rule rule() {
        return super.rule();
    }
    
    @Override
    public Rule getAttributeRule() {
        return mandatory(
                Sequence(
                        basic.spacing(),
                        expr.binary(
                                expr.callable(FilterCall.class),
                                "|"
                        ),
                        action(peek(1, Filter.class).withFilterExpression(expr.pop()))
                ),
                new ParseException("Filter should have at least one function")
        );
    }
    
    public static class Filter extends Content<Filter> {
        private final JtwigPosition position;
        private CompilableExpression filterExpression;

        public Filter(final JtwigPosition position) {
            this.position = position;
        }
        
        public Filter withFilterExpression(final CompilableExpression filterExpression) {
            this.filterExpression = filterExpression;
            return this;
        }

        @Override
        public Renderable compile(final CompileContext context) throws CompileException {
            return new Compiled(position, super.compile(context), filterExpression.compile(context));
        }
    }
    
    private static class Compiled implements Renderable {
        private final JtwigPosition position;
        private final Renderable content;
        private final Expression expression;

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
                context.write(((FilterCall.Compiled)expression).passLeft(valueOf(outputStream.toString())).calculate(context).toString().getBytes());
            } catch (CalculateException | IOException e) {
                throw new RenderException(e);
            }
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