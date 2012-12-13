package com.lyncode.jtwig.functions;

import java.util.List;

import com.lyncode.jtwig.exceptions.FunctionException;

public class StartsWith extends Function {

	@Override
	public Object apply(List<Object> arguments) throws FunctionException {
		if (arguments.size() != 2)
			throw new FunctionException("Equal function must receive two arguments");
		
		
		Boolean b = new Boolean(arguments.get(0).toString().startsWith(arguments.get(1).toString()));
		return b;
	}

}
