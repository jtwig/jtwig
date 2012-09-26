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

import com.lyncode.jtwig.elements.Function;
import com.lyncode.jtwig.elements.ObjectList;
import com.lyncode.jtwig.elements.ObjectMap;
import com.lyncode.jtwig.elements.Variable;
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
	
	public Object evaluate (Object input) throws JtwigRenderException {
		if (input instanceof Variable) {
			return this.evaluate(((Variable) input).getName());
		} else if (input instanceof ObjectList) {
			ObjectList l = (ObjectList) input;
			ObjectList newL = new ObjectList();
			for (Object obj : l)
				newL.add(this.evaluate(obj));
			return newL;
		} else if (input instanceof ObjectMap) {
			ObjectMap m = (ObjectMap) input;
			ObjectMap newM = new ObjectMap();
			for (String key : m.keySet())
				newM.add(key, this.evaluate(m.get(key)));
			return newM;
		} else if (input instanceof Function) {
			
		}
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
				throw new JtwigRenderException("Unable to find Key '"+part+"' in Map "+context.toString());
		} else {
			Field f = ReflectionUtils.findField(context.getClass(), part);
			if (f != null) return ReflectionUtils.getField(f, context);
			
			Method[] methods = context.getClass().getMethods();
			for (Method met : methods) {
				if (met.getName().toLowerCase().equals(part.toLowerCase()))
					return ReflectionUtils.invokeMethod(met, context);
				if (met.getName().toLowerCase().equals("get" + part.toLowerCase()))
					return ReflectionUtils.invokeMethod(met, context);
				if (met.getName().toLowerCase().equals("is" + part.toLowerCase()))
					return ReflectionUtils.invokeMethod(met, context);
			}
			
			throw new JtwigRenderException();
		}
	}
	
	
}
