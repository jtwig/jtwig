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

package org.jtwig.extension.core.functions;

import java.util.Collection;
import java.util.Collections;
import org.jtwig.Environment;
import org.jtwig.extension.api.functions.Function;
import org.jtwig.extension.api.functions.FunctionException;
import org.jtwig.render.RenderContext;
import org.jtwig.util.ObjectExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://twig.sensiolabs.org/doc/functions/attribute.html
 * 
 * According to the Twig documentation, the attribute function can be used
 * access a dynamic attribute of a variable.
 */
public class AttributeFunction implements Function {
    private static final Logger LOGGER = LoggerFactory.getLogger(AttributeFunction.class);

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object... args) throws FunctionException {
        if (args.length < 2) {
            throw new FunctionException("The attribute function takes at least two arguments (the variable and the attributes)");
        }

        Object obj = args[0];
        String attr = args[1].toString();
        Collection params = args.length > 2 ? (Collection)args[2] : Collections.EMPTY_LIST;
        
        try {
            return new ObjectExtractor(ctx, obj).extract(attr, params.toArray(new Object[0]));
        } catch (ObjectExtractor.ExtractException ex) {
            LOGGER.warn("Unable to retrieve attribute {} from object of type {}", attr, obj.getClass().getName());
        }
        return null;
    }
    
}
