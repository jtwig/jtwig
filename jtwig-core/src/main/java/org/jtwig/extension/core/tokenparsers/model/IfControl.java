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

package org.jtwig.extension.core.tokenparsers.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jtwig.compile.CompileContext;
import org.jtwig.content.api.Renderable;
import org.jtwig.content.model.compilable.AbstractElement;
import org.jtwig.content.model.compilable.Content;
import org.jtwig.content.model.tag.TagInformation;
import org.jtwig.content.model.tag.WhiteSpaceControl;
import org.jtwig.exception.CalculateException;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.RenderException;
import org.jtwig.expressions.api.CompilableExpression;
import org.jtwig.expressions.api.Expression;
import org.jtwig.render.RenderContext;
import static org.jtwig.util.TypeUtil.toBoolean;
import org.slf4j.LoggerFactory;

public class IfControl extends AbstractElement {
    private List<Case> cases = new ArrayList<>();

    public IfControl add (Case aCase) {
        cases.add(aCase);
        return this;
    }

    @Override
    public TagInformation tag() {
        return new TagInformation(){
            @Override
            public WhiteSpaceControl whiteSpaceControl() {
                return new WhiteSpaceControl(){

                    @Override
                    public WhiteSpaceControl trimBeforeOpen(boolean value) {
                        return super.trimBeforeOpen(value);
                    }
                    
                    @Override
                    public boolean trimBeforeOpen() {
                        return cases.get(0).tag().whiteSpaceControl().trimBeforeOpen();
                    }

                    @Override
                    public boolean trimAfterOpen() {
                        return cases.get(0).tag().whiteSpaceControl().trimAfterOpen();
                    }

                    @Override
                    public boolean trimBeforeClose() {
                        return cases.get(cases.size()-1).tag().whiteSpaceControl().trimBeforeClose();
                    }

                    @Override
                    public boolean trimAfterClose() {
                        return cases.get(cases.size()-1).tag().whiteSpaceControl().trimAfterClose();
                    }
                };
            }
        };
    }

    @Override
    public Renderable compile(final CompileContext context) throws CompileException {
        List<CompiledCase> cases = new ArrayList<>();

        for (Case aCase : this.cases)
            cases.add((CompiledCase) aCase.compile(context));

        return new Compiled(cases);
    }

    public static class Case extends Content<Case> {
        private CompilableExpression expression;

        public Case(CompilableExpression condition) {
            this.expression = condition;
        }

        @Override
        public Renderable compile(CompileContext context) throws CompileException {
            return new CompiledCase(expression.compile(context), super.compile(context));
        }
    }

    private static class Compiled implements Renderable {
        private final Collection<CompiledCase> cases;

        public Compiled(Collection<CompiledCase> cases) {
            this.cases = cases;
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            for (CompiledCase aCase : cases) {
                if (aCase.conditionIsTrue(context)) {
                    aCase.render(context);
                    break;
                }
            }
        }


    }

    private static class CompiledCase implements Renderable {
        private final Expression expression;
        private final Renderable content;

        public CompiledCase(Expression expression, Renderable content) {
            this.expression = expression;
            this.content = content;
        }

        public boolean conditionIsTrue (RenderContext context) throws RenderException {
            try {
                return toBoolean(expression.calculate(context));
            } catch (CalculateException e) {
                throw new RenderException(e);
            }
        }

        @Override
        public void render(RenderContext context) throws RenderException {
            content.render(context);
        }
    }

}
