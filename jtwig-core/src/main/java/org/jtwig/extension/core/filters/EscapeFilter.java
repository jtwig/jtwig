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

package org.jtwig.extension.core.filters;

import static java.util.Arrays.asList;
import java.util.List;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jtwig.Environment;
import org.jtwig.extension.api.filters.Filter;
import org.jtwig.render.RenderContext;
import org.jtwig.types.Undefined;

public class EscapeFilter implements Filter {

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object left, Object... args) {
        if (left == null || left instanceof Undefined) {
            return null;
        }
        
        String input = left.toString();
        String strategy = args.length > 0 ? args[0].toString() : EscapeStrategy.HTML.name();
        
        switch (EscapeStrategy.strategyByName(strategy.toLowerCase())) {
            case JAVASCRIPT:
                return StringEscapeUtils.escapeEcmaScript(input);
            case XML:
                return StringEscapeUtils.escapeXml(input);
            case HTML: // Default html
            default:
                return StringEscapeUtils.escapeHtml4(input);
        }
    }

    enum EscapeStrategy {
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
            throw new IllegalArgumentException(String.format("Unknown strategy '%s'", name));
        }
    }
    
}