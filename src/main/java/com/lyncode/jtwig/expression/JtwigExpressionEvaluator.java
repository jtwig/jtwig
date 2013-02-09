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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.ReflectionUtils;

import com.lyncode.jtwig.elements.ObjectList;
import com.lyncode.jtwig.elements.ObjectMap;
import com.lyncode.jtwig.elements.Variable;
import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.render.Calculable;

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
	
	public Object evaluate (HttpServletRequest req, Object input) throws JtwigRenderException {
		if (input instanceof Variable) {
			return this.evaluate(((Variable) input).getName());
		} else if (input instanceof ObjectList) {
			ObjectList l = (ObjectList) input;
			ObjectList newL = new ObjectList();
			for (Object obj : l)
				newL.add(this.evaluate(req, obj));
			return newL;
		} else if (input instanceof ObjectMap) {
			ObjectMap m = (ObjectMap) input;
			ObjectMap newM = new ObjectMap();
			for (String key : m.keySet())
				newM.add(key, this.evaluate(req, m.get(key)));
			return newM;
		} else if (input instanceof Calculable) {
			return ((Calculable) input).calculate(req, this.model);
		} 
		return input;
	}
	
	public Object evaluate (String variable) throws JtwigRenderException {
		String[] parts = variable.split(Pattern.quote("."));
		Object context = model;
		
		for (String part : parts) {
			try {
				context = evaluate(context, part.trim());
			} catch (JtwigRenderException e) {
				log.debug(part, e);
				return null;
			}
		}
		
		return context;
	}

	public static Object evaluate(Object context, String part) throws JtwigRenderException {
		if (context == null) return null;
		if (context instanceof Map<?, ?>) {
			log.debug("Trying to get "+part+" on Map");
			
			if (((Map<?, ?>) context).containsKey(part)) {
				Object obja = ((Map<?, ?>) context).get(part);
				if (obja != null) log.debug("Value: "+obja.toString());
				else log.debug("Value: NULL");
				return obja;
			} else
				throw new JtwigRenderException("Unable to find Key '"+part+"' in Map "+context.toString());
		} else {
			Field[] fields = context.getClass().getFields();
			for (Field f : fields) {
				if (Modifier.isPublic(f.getModifiers())) {
					if (f.getName().toLowerCase().equals(part.toLowerCase()))
						return ReflectionUtils.getField(f, context);
				}
			}
			Method[] methods = context.getClass().getMethods();
			for (Method met : methods) {
				if (Modifier.isPublic(met.getModifiers())) {
					boolean call = false;
					if (met.getName().toLowerCase().equals(part.toLowerCase()))
						call = true;
					else if (met.getName().toLowerCase().equals("get" + part.toLowerCase()))
						call = true;
					else if (met.getName().toLowerCase().equals("is" + part.toLowerCase()))
						call = true;
					
					if (call && met.getParameterTypes().length == 0) {
						try {
							return met.invoke(context, (Object[]) null);
						} catch (Exception e) {
							log.debug(e.getMessage(), e);
						} 
						break;
					}
				}
			}
			
			throw new JtwigRenderException("Unable to find Key '"+part+"' in Object "+context.toString());
		}
	}
	
	public static Object evaluate (Object obj, String name, List<Object> arguments) {
		log.debug("Object to call: "+obj);
		String last = name.toLowerCase();
		log.debug("Trying to execute method: "+last);
		for (Method m : obj.getClass().getMethods()) {
			if (m.getName().toLowerCase().equals(last) ||
					m.getName().toLowerCase().equals("get"+last) || 
					m.getName().toLowerCase().equals("is"+last) ||
					m.getName().toLowerCase().equals("has"+last)) {
				if (m.getParameterTypes().length == arguments.size()) {
					try {
						return m.invoke(obj, arguments.toArray());
					} catch (IllegalArgumentException e) {
						log.error(e.getMessage(), e);
					} catch (IllegalAccessException e) {
						log.error(e.getMessage(), e);
					} catch (InvocationTargetException e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		}
		return null;
	}
}
