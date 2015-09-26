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

import org.jtwig.extension.core.tokenparsers.model.Block;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.exception.ParseException;
import org.jtwig.extension.api.tokenparser.TokenParser;
import org.jtwig.loader.Loader;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.parboiled.Rule;
import org.parboiled.annotations.Label;

public class BlockDefinitionParser extends TokenParser {

    public BlockDefinitionParser(Loader.Resource resource,
            JtwigContentParser content, JtwigBasicParser basic,
            JtwigExpressionParser expr, JtwigTagPropertyParser tag) {
        super(resource, content, basic, expr, tag);
    }

    @Label("Block rule")
    @Override
    public Rule rule() {
        return Sequence(
                content.openCode(),
                basic.keyword("block"),
                basic.spacing(),
                mandatory(
                        Sequence(
                                expr.identifierAsString(),
                                push(new Block(currentPosition(), expr.popIdentifierAsString())),
                                basic.spacing(),
                                action(content.beforeBeginTrim()),
                                content.closeCode(),
                                action(content.afterBeginTrim()),
                                content.content(),
                                action(peek(1, Block.class).withContent(pop(Sequence.class))),
                                content.openCode(),
                                basic.keyword("endblock"),
                                basic.spacing(),
                                Optional(
                                        expr.identifierAsString(),
                                        basic.spacing(),
                                        assertEqual(
                                                peek(1, Block.class).name(),
                                                expr.popIdentifierAsString()
                                        )
                                ),
                                action(content.beforeEndTrim()),
                                content.closeCode(),
                                action(content.afterEndTrim())
                        ),
                        new ParseException("Wrong block syntax")
                )
        );
    }

    @Override
    public java.lang.String[] keywords() {
        return new String[]{"block", "endblock"};
    }

    @Override
    public boolean addToContent() {
        return true;
    }

    @Override
    public boolean addToTracker() {
        return true;
    }

    public boolean assertEqual(String value1, String value2) {
        if (!value1.equals(value2)) {
            return throwException(new ParseException("Start statement and ending block names do not match"));
        } else {
            return true;
        }
    }
}
