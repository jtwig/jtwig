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

package com.lyncode.jtwig.tree.value;

public enum Operator {
    ADD("+"),
    SUB("-"),
    INT_TIMES("**"),
    TIMES("*"),
    INT_DIV("//"),
    DIV("/"),
    MOD("%"),

    GT(">"),
    LT("<"),
    GTE(">="),
    LTE("<="),

    AND("and"),
    OR("or"),

    EQUAL("=="),
    DIFF("!="),

    NOT("not"),
    STARTS_WITH("starts with"),
    ENDS_WITH("ends with"),
    MATCHES("matches"),
    IN("in");

    public static Operator fromString (String operatorRepresentation) {
        for (Operator op : values())
            if (op.toString().equals(operatorRepresentation))
                return op;

        return null;
    }

    private String representation;

    private Operator(String representation) {
        this.representation = representation;
    }

    public String toString () {
        return representation;
    }
}
