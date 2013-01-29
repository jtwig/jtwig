package com.lyncode.jtwig.functions;

import java.util.List;

import com.lyncode.jtwig.exceptions.FunctionException;

public class DecimalFormat extends Function {

	@Override
	public Object apply(List<Object> arguments) throws FunctionException {
		if (arguments.size() == 2) {
			java.text.DecimalFormat df = new java.text.DecimalFormat((String) arguments.get(1));
			if (arguments.get(0) != null && arguments.get(0) instanceof Number)
				return df.format(arguments.get(0));
			else
				return arguments.get(0);
		} else throw new FunctionException("Round function requires two arguments");
	}

}
