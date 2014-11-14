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

package com.lyncode.jtwig.parser.model;

import com.google.common.base.Function;
import com.lyncode.builder.ListBuilder;

import java.util.List;

public enum JtwigKeyword {
    TRUE("true"),
    FALSE("false"),

    FOR("for"),
    ENDFOR("endfor"),

    IF("if"),
    ENDIF("endif"),
    ELSEIF("elseif"),
    ELSE("else"),

    BLOCK("block"),
    ENDBLOCK("endblock"),

    EXTENDS("extends"),
    IMPORT("import"),

    EMBED("embed"),
    ENDEMBED("endembed"),

    AND("and"),
    OR("or"),
    NULL("null"),

    SET("set"),
    INCLUDE("include"),
    IGNORE_MISSING("ignore missing"),
    EXCLUDE("exclude"),
    IN("in"),
    IS("is"),
    FILTER("filter"),
    ENDFILTER("endfilter"),

    VERBATIM("verbatim"),
    ENDVERBATIM("endverbatim"),

    WITH("with");

    public static String[] keywords () {
        List<String> list = new ListBuilder<JtwigKeyword>().add(JtwigKeyword.values()).build(extractWord());
        return list.toArray(new String[list.size()]);
    }

    private static Function<JtwigKeyword, String> extractWord() {
        return new Function<JtwigKeyword, String>() {
            public String apply(JtwigKeyword elem) {
                return elem.getKeyword();
            }
        };
    }

    private final String keyword;

    JtwigKeyword (String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public String toString () {
        return getKeyword();
    }
}
