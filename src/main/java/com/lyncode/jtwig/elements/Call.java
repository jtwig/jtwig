package com.lyncode.jtwig.elements;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.expression.JtwigExpressionEvaluator;
import com.lyncode.jtwig.render.Argumentable;
import com.lyncode.jtwig.render.Calculable;

public class Call implements Calculable, Argumentable {
	private static Logger log = LogManager.getLogger(Call.class);
	private String name;
	private List<Object> arguments;

	public Call(String name) {
		super();
		this.name = name;
		this.arguments = new ArrayList<Object>();
	}


	public String getName() {
		return name;
	}
	
	public boolean add (Object obj) {
		this.arguments.add(obj);
		return true;
	}


	@Override
	public Object calculate(HttpServletRequest req, Map<String, Object> values)
			throws JtwigRenderException {
		JtwigExpressionEvaluator evaluator = new JtwigExpressionEvaluator(values);
		
		List<Object> args = new ArrayList<Object>();
		for (Object obj : this.arguments)
			args.add(evaluator.evaluate(req, obj));
		
		String part = this.getName().substring(0, this.getName().lastIndexOf('.'));
		Object obj = evaluator.evaluate(part);
		if (obj != null) {
			log.debug("Object to call: "+obj);
			String last = this.getName().substring(this.getName().lastIndexOf('.') + 1).toLowerCase();
			log.debug("Trying to execute method: "+last);
			for (Method m : obj.getClass().getMethods()) {
				if (last.equals(m.getName().toLowerCase()) ||
					last.equals("get"+m.getName().toLowerCase()) || 
					last.equals("is"+m.getName().toLowerCase()) ||
					last.equals("has"+m.getName().toLowerCase())) {
					if (m.getParameterTypes().length == args.size()) {
						try {
							return m.invoke(obj, args.toArray());
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
			log.error("Unable to execute method "+last);
		}
		return null;
	}


	@Override
	public Object calculate(Map<String, Object> values)
			throws JtwigRenderException {
		JtwigExpressionEvaluator evaluator = new JtwigExpressionEvaluator(values);
		
		List<Object> args = new ArrayList<Object>();
		for (Object obj : this.arguments)
			args.add(evaluator.evaluate(obj));
		
		String part = this.getName().substring(0, this.getName().lastIndexOf('.'));
		Object obj = evaluator.evaluate(part);
		if (obj != null) {
			log.debug("Object to call: "+obj);
			String last = this.getName().substring(this.getName().lastIndexOf('.') + 1).toLowerCase();
			log.debug("Trying to execute method: "+last);
			for (Method m : obj.getClass().getMethods()) {
				if (last.equals(m.getName().toLowerCase()) ||
					last.equals("get"+m.getName().toLowerCase()) || 
					last.equals("is"+m.getName().toLowerCase()) ||
					last.equals("has"+m.getName().toLowerCase())) {
					if (m.getParameterTypes().length == args.size()) {
						try {
							return m.invoke(obj, args.toArray());
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
			log.error("Unable to execute method "+last);
		}
		return null;
	}

}
