package com.lyncode.jtwig.functions;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.jtwig.exceptions.FunctionException;

public class GreaterEqualThan extends Function {

	private static Logger log = LogManager.getLogger(GreaterEqualThan.class);
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object apply(List<Object> arguments)
			throws FunctionException {
		if (arguments.size() != 2)
			throw new FunctionException("GreaterThan function must receive two arguments");
		
		Boolean b = new Boolean(false);
		
		if (arguments.get(0) instanceof Comparable) {
			if (arguments.get(1) instanceof Comparable) {
				int r = ((Comparable) arguments.get(0)).compareTo((Comparable)arguments.get(1));
				if (r >= 0) b = new Boolean(true);  
			}
		}
		
		log.debug("Result: "+b.booleanValue());
		return b;
	}
}
