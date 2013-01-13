package com.lyncode.jtwig.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.expression.JtwigExpressionEvaluator;
import com.lyncode.jtwig.render.Argumentable;
import com.lyncode.jtwig.render.Calculable;

public class EncapsuledIdentifier implements Argumentable, Calculable {
	private String identifier;
	private List<Object> arguments;
	private EncapsuledIdentifier next;
	
	public EncapsuledIdentifier (String name) {
		this.identifier = name;
		this.arguments = new ArrayList<Object>();
	}
	
	public String getIdentifier () {
		return identifier;
	}

	@Override
	public boolean add(Object obj) {
		this.arguments.add(obj);
		return true;
	}
	
	public boolean setNext (EncapsuledIdentifier id) {
		next = id;
		return true;
	}
	
	public boolean hasArguments () {
		return !(this.arguments.isEmpty());
	}

	@Override
	public Object calculate(HttpServletRequest req, Map<String, Object> values)
			throws JtwigRenderException {
		JtwigExpressionEvaluator evaluator = new JtwigExpressionEvaluator(values);
		Object obj = evaluator.evaluate(this.getIdentifier());
		if (next != null) {
			EncapsuledIdentifier tmp = next;
			while (tmp != null) {
				List<Object> args = new ArrayList<Object>();
				for (Object ob : this.arguments)
					args.add(evaluator.evaluate(req, ob));
					
				obj = JtwigExpressionEvaluator.evaluate(obj, tmp.getIdentifier(), args);
				tmp = tmp.next;
			}
		}
		return obj;
	}
}
