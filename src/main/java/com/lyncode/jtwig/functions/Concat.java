package com.lyncode.jtwig.functions;

import java.util.List;

import com.lyncode.jtwig.exceptions.FunctionException;

public class Concat extends Function {

	@Override
	public Object apply(List<Object> arguments) throws FunctionException {
		String result = "";
		if (arguments.size() == 0)
			throw new FunctionException("Not function must receive at least one argument");
		for (Object arg : arguments) {
			result += arg.toString();
		}
		return result;
	}

}
