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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;

import com.lyncode.jtwig.exceptions.FunctionException;

/**
 * @author "Joao Melo <jmelo@lyncode.com>"
 *
 */
public class Message extends Function {
	
	@Autowired(required=false) LocaleResolver localeResolver;
	@Autowired(required=false) MessageSource messageSource;
	@Autowired(required=false) HttpServletRequest request;
	
	@Override
	public Object apply(List<Object> arguments) throws FunctionException {
		
		if (localeResolver == null)
			return arguments.get(0);
		
		if (messageSource == null)
			return arguments.get(0);
				
		if (request == null)
			return arguments.get(0);
				
		if (arguments.isEmpty()) throw new FunctionException("Invalid number of arguments");
		List<Object> args = new ArrayList<Object>();
		for (int i = 1; i < arguments.size(); i++)
			args.add(arguments.get(i));
		
		if (arguments.get(0) instanceof String) {
			return messageSource.getMessage((String) arguments.get(0), args.toArray(), localeResolver.resolveLocale(request));
		} else throw new FunctionException("First parameter must be a string");
	}

}
