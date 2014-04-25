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

package com.lyncode.jtwig.addons.filter;

import com.lyncode.jtwig.addons.Addon;
import com.lyncode.jtwig.addons.AddonParser;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.expressions.model.Operator;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;
import org.parboiled.Rule;

public class FilterParser extends AddonParser {
    public FilterParser(JtwigResource resource, ParserConfiguration configuration) {
        super(resource, configuration);
    }

    @Override
    public Addon instance() {
        return null;
    }

    @Override
    public String beginKeyword() {
        return "filter";
    }

    @Override
    public String endKeyword() {
        return "endfilter";
    }

    @Override
    public Rule startRule() {
        return mandatory(
                Sequence(
                        expressionParser().binary(
                                FirstOf(
                                        expressionParser().functionWithBrackets(),
                                        expressionParser().variable()
                                ),
                                Operator.COMPOSITION
                        ),
                        push(new Filter(expressionParser().pop()))
                ),
                new ParseException("Filter should have at least one function")
        );
    }
}
