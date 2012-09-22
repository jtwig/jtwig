package com.lyncode.jtwig.tree;

import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.jtwig.render.JtwigConstantRender;
import com.lyncode.jtwig.render.JtwigRender;

public class JtwigConstant<T> extends JtwigValue {
	private static Logger log = LogManager.getLogger(JtwigConstant.class);
	private T literal;

	public JtwigConstant(T literal) {
		super();
		this.literal = literal;
		log.debug("Constant of type: "+literal.getClass().getSimpleName()+" = "+literal.toString());
	}

	public T getLiteral() {
		return literal;
	}
	
	public String toString () {
		return literal.getClass().getSimpleName()+" = "+literal.toString();
	}

	@Override
	public JtwigRender<? extends JtwigElement> renderer(Map<String, Object> map) {
		return new JtwigConstantRender(map, this);
	}
}
