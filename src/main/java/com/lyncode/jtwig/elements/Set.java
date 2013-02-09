package com.lyncode.jtwig.elements;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.render.Calculable;
import com.lyncode.jtwig.render.Renderable;

public class Set implements Renderable {
	private String name;
	private Object value;
	
	public Set(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String render(HttpServletRequest req, Map<String, Object> model) throws JtwigRenderException {
		Object values = null;
		if (this.value instanceof Calculable) {
			values = ((Calculable) this.value).calculate(req, model);
		} else values = this.value;
		
		model.put(this.getName(), values);
		return "";
	}
}
