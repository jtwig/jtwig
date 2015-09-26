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
import org.jtwig.exception.ParseException;
import org.jtwig.exception.ResourceException;
import org.jtwig.extension.api.functions.Function;
import org.jtwig.extension.api.functions.FunctionException;
import org.jtwig.loader.Loader;
import org.jtwig.loader.impl.StringLoader;
import org.jtwig.render.RenderContext;

public class TemplateFromStringFunction implements Function {

    @Override
    public Object evaluate(Environment env, RenderContext ctx, Object... args) throws FunctionException {
        if (args.length == 0 || args[0] == null
                || !(args[0] instanceof CharSequence)) {
            throw new FunctionException("The template_from_string function requires a string argument");
        }
        
        try {
            Loader.Resource resource = new StringLoader.StringResource(args[0].toString());
            return env.parse(resource);
        } catch (ResourceException | ParseException ex) {
            throw new FunctionException(ex);
        }
    }
    
}
