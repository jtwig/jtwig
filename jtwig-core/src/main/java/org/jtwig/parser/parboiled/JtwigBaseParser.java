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

package org.jtwig.parser.parboiled;

import org.jtwig.content.model.Template;
import org.jtwig.exception.ParseBypassException;
import org.jtwig.exception.ParseException;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.resource.JtwigResource;
import org.parboiled.BaseParser;
import org.parboiled.Rule;

public class JtwigBaseParser<V> extends BaseParser<V> {
    final JtwigResource resource;

    public JtwigBaseParser(JtwigResource resource) {
        this.resource = resource;
    }

    public boolean action (Object action) {
        return true;
    }

    public JtwigPosition currentPosition () {
        return new JtwigPosition(
                resource,
                currentTemplate(),
                position().line,
                position().column
        );
    }
    
    Template currentTemplate() {
        for (Object obj : this.getContext().getValueStack()) {
            if (obj instanceof Template) {
                return (Template)obj;
            }
        }
        return null;
    }

    public JtwigResource getResource() {
        return resource;
    }



    boolean throwException(ParseException exception) throws ParseBypassException {
        throw new ParseBypassException(exception);
    }

    <T> T peek(int position, Class<T> typeClass) {
        return typeClass.cast(peek(position));
    }

    <T> T peek(Class<T> typeClass) {
        return peek(0, typeClass);
    }

    <T> T pop(int position, Class<T> typeClass) {
        return typeClass.cast(pop(position));
    }

    <T> T pop(Class<T> typeClass) {
        return pop(0, typeClass);
    }

    public Rule mandatory(Rule rule, ParseException exception) {
        return FirstOf(
                rule,
                throwException(exception)
        );
    }
}
