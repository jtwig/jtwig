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

package org.jtwig.extension.spring.filters;

import javax.servlet.http.HttpServletRequest;
import org.jtwig.extension.api.filters.FilterException;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;

public class TranslateFilter extends AbstractSpringFilter {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;
    
    public TranslateFilter(final MessageSource messageSource, final LocaleResolver localeResolver) {
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    @Override
    public Object evaluate(HttpServletRequest request, Object left, Object... args) throws FilterException {
        if (messageSource == null)
            throw new FilterException("In order to use the translate filter, a bean of type " + MessageSource.class.getName() + " must be configured");
        if (localeResolver == null)
            throw new FilterException("In order to use the translate filter, a bean of type " + LocaleResolver.class.getName() + " must be configured");

        return messageSource.getMessage(left.toString(), args, localeResolver.resolveLocale(request));
    }
    
//
//    @JtwigFunction(name = "translate", aliases = {"message", "trans"})
//    public String translate(HttpServletRequest request, @Parameter String input, @Parameter Object... rest) throws FunctionException {
//        if (messageSource == null)
//            throw new FunctionException("In order to use the translate function, a bean of type " + MessageSource.class.getName() + " must be configured");
//        if (localeResolver == null)
//            throw new FunctionException("In order to use the translate function, a bean of type " + LocaleResolver.class.getName() + " must be configured");
//
//        return messageSource.getMessage(input, rest, localeResolver.resolveLocale(request));
//    }
}