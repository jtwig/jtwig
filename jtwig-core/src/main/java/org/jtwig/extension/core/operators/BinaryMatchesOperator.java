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

package org.jtwig.extension.core.operators;

import jregex.Matcher;
import jregex.Pattern;
import org.jtwig.extension.api.operator.BinaryOperator;
import org.jtwig.parser.model.JtwigPosition;
import org.jtwig.render.RenderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinaryMatchesOperator extends BinaryOperator {
    private static final Logger LOGGER = LoggerFactory.getLogger(BinaryMatchesOperator.class);

    public BinaryMatchesOperator(String name, int precedence) {
        super(name, precedence);
    }

    @Override
    public Boolean render(RenderContext ctx, JtwigPosition pos, Object left, Object right) {
        if (left == null) {
            return false;
        }
        
        // Grab and massage the regex
        String regex = right.toString();
        if (!regex.endsWith(String.valueOf(regex.charAt(0)))) {
            LOGGER.warn("The regex '{}' is not delimited. Suggest you wrap with slashes to look like: {}", regex, "/"+regex+"/");
            return null;
        }
        regex = regex.substring(1, regex.length() - 1);
        
        Matcher matcher = new Pattern(regex, Pattern.DOTALL | Pattern.MULTILINE).matcher(left.toString());
        if (!regex.startsWith("^") || !regex.endsWith("$")) {
            return matcher.find();
        }
        return matcher.find();
    }
    
}