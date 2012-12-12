package com.lyncode.jtwig.functions;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lyncode.jtwig.exceptions.FunctionException;

public class Count extends Function {

	@Override
	public Object apply(HttpServletRequest req, List<Object> arguments)
			throws FunctionException {
		if (arguments.size() != 1) throw new FunctionException(this.getClass().getSimpleName()+" function requires only one parameter");
		Object obj = arguments.get(0);
		if (obj == null) return 0;
		else if (obj instanceof Collection<?>) return ((Collection<?>)obj).size();
		else if (obj instanceof Map<?,?>) return ((Map<?,?>)obj).size();
		else return 1;
	}

}
