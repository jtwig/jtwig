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
package com.lyncode.jtwig.expression;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.ReflectionUtils;

import com.lyncode.jtwig.exceptions.JtwigRenderException;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class JtwigExpressionEvaluator {
	private static Logger log = LogManager.getLogger(JtwigExpressionEvaluator.class);
	private Map<String, Object> model;

	public JtwigExpressionEvaluator(Map<String, Object> model) {
		super();
		this.model = model;
	}
	
	public Object evaluate (String variable) throws JtwigRenderException {
		String[] parts = variable.split(Pattern.quote("."));
		Object context = model;
		
		for (String part : parts) {
			try {
				context = this.evaluate(context, part.trim());
			} catch (JtwigRenderException e) {
				log.debug(part, e);
				throw new JtwigRenderException("Unable to evaluate variable "+variable, e);
			}
		}
		
		return context;
	}

	private Object evaluate(Object context, String part) throws JtwigRenderException {
		if (context instanceof Map<?, ?>) {
			System.out.println("Trying to get "+part+" on Map:");
			for (Object k : ((Map<?, ?>) context).keySet())
				System.out.println(k.toString()+ " = "+((Map<?, ?>) context).get(k));
			
			if (((Map<?, ?>) context).containsKey(part))
				return ((Map<?, ?>) context).get(part);
			else
				throw new JtwigRenderException("Unable to find Key '"+part+"' in Map");
		} else {
			Field f = ReflectionUtils.findField(context.getClass(), part);
			if (f != null) return ReflectionUtils.getField(f, context);
			
			String name = WordUtils.capitalizeFully(part);
			
			Method m = ReflectionUtils.findMethod(context.getClass(), "get" + name);
			if (m == null) m = ReflectionUtils.findMethod(context.getClass(), "is" + name);
			if (m != null) return ReflectionUtils.invokeMethod(m, context);
			
			throw new JtwigRenderException();
		}
	}
	
	
}
