package com.lyncode.jtwig.functions;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.jtwig.exceptions.FunctionException;

public class Equal extends Function {

	private static Logger log = LogManager.getLogger(Equal.class);
	
	@Override
	public Object apply(List<Object> arguments)
			throws FunctionException {
		if (arguments.size() != 2)
			throw new FunctionException("Equal function must receive two arguments");
		
		log.debug("Element 1: "+arguments.get(0).toString());
		log.debug("Element 2: "+arguments.get(1).toString());
		
		Boolean b = new Boolean(arguments.get(0).equals(arguments.get(1)));
		log.debug("Result: "+b.booleanValue());
		return b;
	}
}
