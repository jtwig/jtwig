package com.lyncode.jtwig.tree;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
}
