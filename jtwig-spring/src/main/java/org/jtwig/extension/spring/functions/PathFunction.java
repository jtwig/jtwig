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
import org.jtwig.util.UrlPath;

public class PathFunction extends AbstractSpringFunction {

    @Override
    public String evaluate(final Environment env, final RenderContext ctx,
            final HttpServletRequest request, final Object... args)
            throws FunctionException {
        UrlPath url = new UrlPath().append(request.getContextPath());
        if (args.length > 0) {
            url.append(args[0].toString());
        }
        return url.toString();
    }
    
//    @JtwigFunction(name = "path")
//    public String path(HttpServletRequest request, @Parameter String input) {
//        return new UrlPath().append(request.getContextPath()).append(input).toString();
//    }
//
//    @JtwigFunction(name = "path")
//    public String path(HttpServletRequest request) {
//        return new UrlPath().append(request.getContextPath()).toString();
//    }
    
}