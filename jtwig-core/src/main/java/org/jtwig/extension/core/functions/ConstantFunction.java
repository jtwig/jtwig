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

import org.jtwig.Environment;
import org.jtwig.extension.api.functions.Function;
import org.jtwig.extension.api.functions.FunctionException;
import org.jtwig.render.RenderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://twig.sensiolabs.org/doc/functions/constant.html
 * 
 * According to the Twig documentation, the constant function returns the value
 * of the specified constant.
 */
public class ConstantFunction implements Function {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConstantFunction.class);

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object... args) throws FunctionException {
        try {
            if (args.length == 0) {
                LOGGER.warn("Missing argument 1 for constant()");
                return null;
            }
            String name = args[0].toString();
            Object obj = args.length > 1 ? args[1] : null;
            
            Class cls = null;
            if (obj != null) {
                cls = obj.getClass();
            } else {
                cls = Class.forName(name.substring(0, name.lastIndexOf('.')));
                name = name.substring(name.lastIndexOf('.')+1);
            }
            LOGGER.error("Class: {}", cls);
            LOGGER.error("Name: {}", name);
            
            return cls.getDeclaredField(name).get(null);
        } catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
            LOGGER.warn("Couldn't find constant {}", args[0]);
        }
        return null;
    }
    
}