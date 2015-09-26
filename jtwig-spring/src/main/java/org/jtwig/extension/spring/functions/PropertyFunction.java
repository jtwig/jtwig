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

package org.jtwig.extension.spring.functions;

import javax.servlet.http.HttpServletRequest;
import org.jtwig.Environment;
import org.jtwig.extension.api.functions.FunctionException;
import org.jtwig.render.RenderContext;

public class PropertyFunction extends AbstractSpringFunction {
    private final org.springframework.core.env.Environment springEnv;
    
    public PropertyFunction(final org.springframework.core.env.Environment springEnv) {
        this.springEnv = springEnv;
    }

    @Override
    public Object evaluate(final Environment env, final RenderContext ctx,
            final HttpServletRequest request, final Object... args)
            throws FunctionException {
        return springEnv.getProperty(args[0].toString());
    }

//    @JtwigFunction(name = "property")
//    public Object property(@Parameter String name) throws FunctionException {
//        if (environment == null) throw new FunctionException("Unable to retrieve Environment bean");
//        else return environment.getProperty(name);
//    }
    
}