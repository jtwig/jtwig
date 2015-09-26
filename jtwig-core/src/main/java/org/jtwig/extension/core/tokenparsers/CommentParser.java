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

import org.jtwig.content.model.compilable.Comment;
import org.jtwig.extension.api.tokenparser.TokenParser;
import org.jtwig.loader.Loader;
import org.jtwig.parser.model.JtwigSymbol;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.parboiled.Rule;

public class CommentParser extends TokenParser {

    public CommentParser(Loader.Resource resource,
            JtwigContentParser content, JtwigBasicParser basic,
            JtwigExpressionParser expr, JtwigTagPropertyParser tag) {
        super(resource, content, basic, expr, tag);
    }

    @Override
    public Rule rule() {
        return Sequence(
                push(new Comment()),
                openComment(),
                action(content.beforeBeginTrim()),
                ZeroOrMore(
                        TestNot(
                                Sequence(
                                        basic.symbol(JtwigSymbol.MINUS),
                                        basic.closeComment()
                                )
                        ),
                        TestNot(
                                basic.closeComment()
                        ),
                        ANY
                ),
                closeComment(),
                action(content.afterEndTrim())
        );
    }

    public Rule openComment() {
        return Sequence(
                basic.openComment(),
                tag.property(),
                basic.spacing()
        );
    }

    public Rule closeComment() {
        return Sequence(
                tag.property(),
                basic.closeComment()
        );
    }
}