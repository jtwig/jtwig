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

import org.jtwig.content.model.Template;
import org.jtwig.content.model.compilable.Comment;
import org.jtwig.exception.ParseException;
import org.jtwig.expressions.model.Constant;
import org.jtwig.extension.api.tokenparser.TokenParser;
import org.jtwig.loader.Loader;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.parboiled.Rule;
import org.parboiled.annotations.Label;

public class ExtendsStatementParser extends TokenParser {

    public ExtendsStatementParser(Loader.Resource resource,
            JtwigContentParser content, JtwigBasicParser basic,
            JtwigExpressionParser expr, JtwigTagPropertyParser tag) {
        super(resource, content, basic, expr, tag);
    }

    @Label("Extends rule")
    @Override
    public Rule rule() {
        return Sequence(
                content.openCode(),
                basic.spacing(),
                basic.keyword("extends"),
                mandatory(
                        Sequence(
                                basic.spacing(),
                                FirstOf(
                                        Sequence(
                                                basic.stringLiteral(),
                                                expr.push(new Constant<>(basic.pop()))
                                        ),
                                        expr.expression()
                                ),
                                action(peek(2, Template.class).setParent(expr.pop())),
                                basic.spacing(),
                                content.closeCode()
                        ),
                        new ParseException("Wrong extends syntax")
                )
        );
    }

    @Override
    public java.lang.String[] keywords() {
        return new String[]{"extends"};
    }

    @Override
    public boolean addToContent() {
        return false;
    }

    @Override
    public boolean addToTracker() {
        return false;
    }
    
}
