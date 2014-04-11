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

package com.lyncode.jtwig.functions;

import com.lyncode.jtwig.exceptions.AssetResolveException;
import com.lyncode.jtwig.functions.annotations.JtwigFunction;
import com.lyncode.jtwig.functions.annotations.Parameter;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import com.lyncode.jtwig.services.api.assets.AssetResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class SpringFunctions {
    @Autowired(required = false)
    private AssetResolver assetResolver;

    @Autowired(required = false)
    private MessageSource messageSource;

    @Autowired(required = false)
    private LocaleResolver localeResolver;

    @Autowired(required = false)
    private Environment environment;

    @JtwigFunction(name = "asset")
    public String asset (HttpServletRequest request, @Parameter String input) throws AssetResolveException, FunctionException {
        if (assetResolver == null) throw new FunctionException("In order to use the asset function, a bean of type "+AssetResolver.class.getName()+" must be configured");
        return new File(request.getContextPath(), assetResolver.resolve(input)).getPath();
    }

    @JtwigFunction(name = "path")
    public String path(HttpServletRequest request, @Parameter String input) {
        return new File(request.getContextPath(), input).getPath();
    }

    @JtwigFunction(name = "translate", aliases = {"message", "trans"})
    public String translate(HttpServletRequest request, @Parameter String input, @Parameter Object... rest) throws FunctionException {
        if (messageSource == null)
            throw new FunctionException("In order to use the translate function, a bean of type "+MessageSource.class.getName()+" must be configured");
        if (localeResolver == null)
            throw new FunctionException("In order to use the translate function, a bean of type "+LocaleResolver.class.getName()+" must be configured");

        return messageSource.getMessage(input, rest, localeResolver.resolveLocale(request));
    }

    @JtwigFunction(name = "property")
    public Object property (@Parameter String name) throws FunctionException {
        if (environment == null) throw new FunctionException("Unable to retrieve Environment bean");
        else return environment.getProperty(name);
    }
}
