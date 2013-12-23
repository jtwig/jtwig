/**
 * Copyright 2012 Lyncode
 *
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

public enum JtwigSymbol {
    DOT("."),
    OPEN_BRACKET("["),
    CLOSE_BRACKET("]"),
    DIV(":"),
    COMMA(","),
    OPEN_CURLY_BRACKET("{"),
    CLOSE_CURLY_BRACKET("}"),
    OPEN_PARENT("("),
    CLOSE_PARENT(")"),
    DIVISION("/"),
    PLUS("+"),
    MINUS("-"),
    TIMES("*"),
    PERCENTAGE("%"),

    AND("&&"),
    OR("||"),

    AND_WORD("and"),
    OR_WORD("or"),

    EQUALITY("=="),
    DIFFERENT("!="),

    GREAT(">"),
    LESS("<"),

    GREAT_OR_EQUAL(">="),
    LESS_OR_EQUAL("<="),

    NOT("!"),
    NOT_WORD("not"),

    OPEN_FAST("{{"),
    CLOSE_FAST("}}"),

    PIPE("|"),

    CLOSE_CODE("%}"),
    OPEN_CODE("{%"),

    ATTR("="),

    QUOTE("'"),

    TWO_DOTS("..");

    private String symbol;

    JtwigSymbol (String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
