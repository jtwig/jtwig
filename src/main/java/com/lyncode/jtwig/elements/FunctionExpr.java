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
package com.lyncode.jtwig.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.lyncode.jtwig.exceptions.FunctionException;
import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.expression.JtwigExpressionEvaluator;
import com.lyncode.jtwig.functions.Function;
import com.lyncode.jtwig.render.Argumentable;
import com.lyncode.jtwig.render.Calculable;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class FunctionExpr implements Calculable, Argumentable {
	private static Logger log = LogManager.getLogger(FunctionExpr.class);
	private String name;
	private List<Object> arguments;

	public FunctionExpr(String name) {
		super();
		this.name = name;
		this.arguments = new ArrayList<Object>();
		log.debug(this);
	}

	public String getName() {
		return name;
	}
	
	
	
	public List<Object> getArguments() {
		return arguments;
	}

	public boolean add (Object argument) {
		this.arguments.add(argument);
		return true;
	}

	public Object calculate(HttpServletRequest req, Map<String, Object> values) throws JtwigRenderException {
		JtwigExpressionEvaluator evaluator = new JtwigExpressionEvaluator(values);
		AutowireCapableBeanFactory factory = RequestContextUtils.getWebApplicationContext(req).getAutowireCapableBeanFactory();
		String name = WordUtils.capitalizeFully(this.getName().replace('-', ' ').replace('_', ' ')).replaceAll(" ", "");
		String className = Function.class.getPackage().getName() + "." + name;
		try {
			List<Object> args = new ArrayList<Object>();
			
			for (Object arg : this.arguments)
				args.add(evaluator.evaluate(req, arg));
			
			Class<?> loadedClass = this.getClass().getClassLoader().loadClass(className);
			Object obj = loadedClass.newInstance();
			if (obj instanceof Function) {
				factory.autowireBean(obj);
				return ((Function) obj).apply(args);
			} else 
				throw new JtwigRenderException("Unknown function "+name);
		} catch (ClassNotFoundException e) {
			throw new JtwigRenderException(e);
		} catch (InstantiationException e) {
			throw new JtwigRenderException(e);
		} catch (IllegalAccessException e) {
			throw new JtwigRenderException(e);
		} catch (FunctionException e) {
			throw new JtwigRenderException(e);
		}
	}
	

	public Object calculate(HttpServletRequest req, List<Object> calculatedArguments) throws JtwigRenderException {
		AutowireCapableBeanFactory factory = RequestContextUtils.getWebApplicationContext(req).getAutowireCapableBeanFactory();
		String name = WordUtils.capitalizeFully(this.getName().replace('-', ' ').replace('_', ' ')).replaceAll(" ", "");
		String className = Function.class.getPackage().getName() + "." + name;
		try {
			Class<?> loadedClass = this.getClass().getClassLoader().loadClass(className);
			Object obj = loadedClass.newInstance();
			if (obj instanceof Function) {
				factory.autowireBean(obj);
				return ((Function) obj).apply(calculatedArguments);
			} else throw new JtwigRenderException("Unknown function "+name);
		} catch (ClassNotFoundException e) {
			throw new JtwigRenderException(e);
		} catch (InstantiationException e) {
			throw new JtwigRenderException(e);
		} catch (IllegalAccessException e) {
			throw new JtwigRenderException(e);
		} catch (FunctionException e) {
			throw new JtwigRenderException(e);
		}
	}

	public String toString () {
		return "FUNCTION: "+name;
	}
}
