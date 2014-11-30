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

package org.jtwig.addons.filter;

import org.jtwig.addons.Addon;
import org.jtwig.addons.AddonModel;
import org.jtwig.exception.ParseException;
import org.jtwig.expressions.model.Operator;
import org.jtwig.parser.config.ParserConfiguration;
import org.jtwig.resource.JtwigResource;
import org.parboiled.Rule;

public class FilterAddon extends Addon {
    public FilterAddon(JtwigResource resource, ParserConfiguration configuration) {
        super(resource, configuration);
    }

    @Override
    public AddonModel instance() {
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
                                        expressionParser().variableAsFunction()
                                ),
                                Operator.COMPOSITION
                        ),
                        push(new Filter(currentPosition(), expressionParser().pop()))
                ),
                new ParseException("Filter should have at least one function")
        );
    }
}
