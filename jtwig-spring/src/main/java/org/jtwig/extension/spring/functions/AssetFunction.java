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
import org.jtwig.services.api.assets.AssetResolver;
import org.jtwig.util.UrlPath;

public class AssetFunction extends AbstractSpringFunction {
    private final AssetResolver assetResolver;
    
    public AssetFunction(final AssetResolver assetResolver) {
        this.assetResolver = assetResolver;
    }

    @Override
    public Object evaluate(final Environment env, final RenderContext ctx,
            final HttpServletRequest request, final Object... args)
            throws FunctionException {
        if (assetResolver == null)
            throw new FunctionException("In order to use the asset function, a bean of type " + AssetResolver.class.getName() + " must be configured");
        return new UrlPath().append(request.getContextPath()).append(assetResolver.resolve(args[0].toString())).toString();
    }

//    @JtwigFunction(name = "asset")
//    public String asset(HttpServletRequest request, @Parameter String input) throws AssetResolveException, FunctionException {
//        if (assetResolver == null)
//            throw new FunctionException("In order to use the asset function, a bean of type " + AssetResolver.class.getName() + " must be configured");
//        return new UrlPath().append(request.getContextPath()).append(assetResolver.resolve(input)).toString();
//    }
    
}
