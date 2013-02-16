package com.lyncode.jtwig.functions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.lyncode.jtwig.exceptions.FunctionException;

public class DateFormat extends Function {

	@Override
	public Object apply(List<Object> arguments) throws FunctionException {
		if (arguments.size() == 2) {
			if (!(arguments.get(1) instanceof String))
				throw new FunctionException("DateFormat function requires a string as second argument");
			java.text.DateFormat dateFormat = new SimpleDateFormat((String) arguments.get(1));
			if (arguments.get(0) != null && arguments.get(0) instanceof Date)
				return dateFormat.format(arguments.get(0));
			else
				return arguments.get(0);
		} else throw new FunctionException("DateFormat function requires two arguments");
	}

}
