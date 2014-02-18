/**
 * Copyright 2012 Lyncode
 *
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

import com.lyncode.jtwig.functions.exceptions.FunctionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Arrays;
import java.util.Locale;

import static com.lyncode.jtwig.functions.util.Requirements.requires;
import static com.lyncode.jtwig.util.LocalThreadHolder.getServletRequest;
import static org.hamcrest.Matchers.greaterThan;

public class Message extends AutowiredJtwigFunction {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    protected Object call(Object... arguments) throws FunctionException {
        requires(arguments)
                .withNumberOfArguments(greaterThan(1));
        Locale locale = localeResolver.resolveLocale(getServletRequest());
        return messageSource.getMessage(arguments[0].toString(), Arrays.copyOfRange(arguments, 1, arguments.length), locale);
    }
}
