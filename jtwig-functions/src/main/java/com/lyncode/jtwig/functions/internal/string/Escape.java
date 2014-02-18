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

package com.lyncode.jtwig.functions.internal.string;

import com.lyncode.jtwig.functions.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.JtwigFunctionDeclaration;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

import static com.lyncode.jtwig.functions.util.Requirements.between;
import static com.lyncode.jtwig.functions.util.Requirements.requires;
import static java.util.Arrays.asList;

@JtwigFunctionDeclaration(name = "escape", aliases = { "e" })
public class Escape implements JtwigFunction {
    @Override
    public Object execute(Object... arguments) throws FunctionException {
        requires(arguments)
                .withNumberOfArguments(between(1, 2));

        String strategy = "html";
        if (arguments.length == 2)
            strategy = arguments[1].toString().toLowerCase();

        switch (EscapeStrategy.strategyByName(strategy.toLowerCase())) {
            case HTML:
                return StringEscapeUtils.escapeHtml4(arguments[0].toString());
            case JAVASCRIPT:
                return StringEscapeUtils.escapeEcmaScript(arguments[0].toString());
            case XML:
                return StringEscapeUtils.escapeXml(arguments[0].toString());
            default:
                throw new FunctionException("Unknown escaping strategy");
        }

    }

    public static enum EscapeStrategy {
        HTML("html"),
        JAVASCRIPT("js", "javascript"),
        XML("xml");

        private List<String> representations;

        EscapeStrategy(String... representations) {
            this.representations = asList(representations);
        }

        public static EscapeStrategy strategyByName(String name) {
            for (EscapeStrategy escape : EscapeStrategy.values()) {
                if (escape.representations.contains(name))
                    return escape;
            }
            return null;
        }
    }
}
