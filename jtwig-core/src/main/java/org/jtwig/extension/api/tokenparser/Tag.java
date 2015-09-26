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

package org.jtwig.extension.api.tokenparser;

import org.jtwig.content.api.Compilable;
import org.jtwig.content.model.compilable.Content;
import org.jtwig.content.model.compilable.Sequence;
import org.jtwig.exception.ParseException;
import org.jtwig.loader.Loader;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.parser.parboiled.JtwigBasicParser;
import org.jtwig.parser.parboiled.JtwigContentParser;
import org.jtwig.parser.parboiled.JtwigExpressionParser;
import org.jtwig.parser.parboiled.JtwigTagPropertyParser;
import org.parboiled.Rule;

public abstract class Tag extends TokenParser {

    public Tag(Loader.Resource resource, JtwigContentParser content, JtwigBasicParser basic, JtwigExpressionParser expr, JtwigTagPropertyParser tag) {
        super(resource, content, basic, expr, tag);
    }
    
    public abstract String getKeyword();
    
    public String getEndKeyword() {
        return "end"+getKeyword();
    }

    @Override
    public Rule rule() {
        return Sequence(
                action(push(model(currentPosition()))),
                content.openCode(),
                basic.spacing(),
                basic.keyword(getKeyword()),
                basic.spacing(),
                mandatory(
                        Sequence(
                                getAttributeRule().label("Attribute data"),
                                basic.spacing(),
                                content.closeCode(),
                                Optional(
                                        takesContent(peek()),
                                        content.content(),
                                        action(peek(1, Content.class).withContent(pop(Sequence.class)))
                                ).label("Tag content"),
                                getEndKeywordRule().label("Closing tag")
                        ),
                        new ParseException("Wrong tag syntax")
                )
        );
    }
    
    public Rule getAttributeRule() {
        return EMPTY;
    }
    
    public Rule getEndKeywordRule() {
        if (getEndKeyword() == null) {
            return EMPTY;
        }
        return mandatory(
                Sequence(
                        basic.spacing(),
                        content.openCode(),
                        basic.spacing(),
                        basic.keyword(""+getEndKeyword()),
                        basic.spacing(),
                        content.closeCode()
                ),
                new ParseException("End tag '"+getEndKeyword()+"' required but not found")
        );
    }
    
    public abstract Compilable model(JtwigPosition pos);
    
    public boolean takesContent(Compilable compilable) {
        return compilable instanceof Content;
    }

    @Override
    public String[] keywords() {
        if (getEndKeyword() != null) {
            return new String[]{getKeyword(),getEndKeyword()};
        }
        return new String[]{getKeyword()};
    }
    
}