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
import org.jtwig.extension.core.tokenparsers.model.Embed;
import org.jtwig.exception.ParseException;
import org.jtwig.expressions.model.Constant;
import org.jtwig.extension.api.tokenparser.TokenParser;
import org.jtwig.loader.Loader;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;

public class EmbedStatementParser extends TokenParser {
    final BlockDefinitionParser block;

    public EmbedStatementParser(Loader.Resource resource, JtwigContentParser content, JtwigBasicParser basic, JtwigExpressionParser expr, JtwigTagPropertyParser tag) {
        super(resource, content, basic, expr, tag);
        block = Parboiled.createParser(BlockDefinitionParser.class, resource, content, basic, expr, tag);
    }

    @Override
    public Rule rule() {
        return Sequence(
                content.openCode(),
                basic.spacing(),
                basic.keyword("embed"),
                basic.spacing(),
                mandatory(
                        Sequence(
                                basic.stringLiteral(),
                                basic.spacing(),
                                content.closeCode(),
                                push(new Embed(currentPosition()).setParent(new Constant<>(basic.pop()))),
                                ZeroOrMore(
                                        basic.spacing(),
                                        content.addToElementTracker(block.rule(), true)
                                ),
                                basic.spacing(),
                                content.openCode(),
                                basic.spacing(),
                                basic.keyword("endembed"),
                                basic.spacing(),
                                content.closeCode()
                        ),
                        new ParseException("Wrong embed syntax")
                )
        );
    }

    @Override
    public java.lang.String[] keywords() {
        return new String[]{"embed","endembed"};
    }
    
}