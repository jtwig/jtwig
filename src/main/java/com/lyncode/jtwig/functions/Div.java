package com.lyncode.jtwig.functions;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.jtwig.exceptions.FunctionException;

public class Div extends Function {

	private static Logger log = LogManager.getLogger(Div.class);
	
	@Override
	public Object apply(List<Object> arguments)
			throws FunctionException {
		if (arguments.size() != 2)
			throw new FunctionException("Div function must receive two arguments");
		
		Object arg1 = arguments.get(0);
		if (arg1 instanceof Long)
			return ((Long) arg1) / ((Long) arguments.get(1));
		else if (arg1 instanceof Integer)
			return ((Integer) arg1) / ((Integer) arguments.get(1));
		else if (arg1 instanceof Float)
			return ((Float) arg1) / ((Float) arguments.get(1));
		else if (arg1 instanceof Double)
			return ((Double) arg1) / ((Double) arguments.get(1));
		else
			return ((Number) arg1).intValue() / ((Number) arguments.get(1)).intValue();
		
	}
}
