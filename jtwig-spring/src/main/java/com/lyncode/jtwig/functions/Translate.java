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

import com.lyncode.jtwig.functions.annotations.JtwigFunctionDeclaration;
import com.lyncode.jtwig.functions.exceptions.FunctionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static com.lyncode.jtwig.util.LocalThreadHolder.getServletRequest;
import static java.util.Arrays.copyOfRange;

@JtwigFunctionDeclaration(name = "translate", aliases = {"message", "trans"})
public class Translate extends AutowiredJtwigFunction {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Autowired
    private HttpServletRequest request;

    @Override
    public Object call(Object... arguments) throws FunctionException {

        if (arguments.length < 1) throw new FunctionException("Expecting at least one argument");
        else {
            HttpServletRequest request = getServletRequest();
            Locale locale = localeResolver.resolveLocale(request);
            Object[] parameters = copyOfRange(arguments, 1, arguments.length);

            return messageSource.getMessage(String.valueOf(arguments[0]), parameters, locale);
        }
    }
}
