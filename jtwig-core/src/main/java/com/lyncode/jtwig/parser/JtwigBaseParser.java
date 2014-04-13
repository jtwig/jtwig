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

package com.lyncode.jtwig.parser;

import com.lyncode.jtwig.exception.ParseBypassException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.parser.positioning.Position;
import com.lyncode.jtwig.resource.JtwigResource;
import org.parboiled.BaseParser;

public class JtwigBaseParser<V> extends BaseParser<V> {
    final JtwigResource resource;

    public JtwigBaseParser(JtwigResource resource) {
        this.resource = resource;
    }

    public boolean action (Object action) {
        return true;
    }

    public Position currentPosition () {
        return new Position(
                resource,
                position().line,
                position().column
        );
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
}
