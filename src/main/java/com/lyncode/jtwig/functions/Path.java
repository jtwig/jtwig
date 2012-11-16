package com.lyncode.jtwig.functions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.lyncode.jtwig.exceptions.FunctionException;

public class Path extends Function {

	@Override
	public Object apply(HttpServletRequest req, List<Object> arguments)
			throws FunctionException {
		if (arguments.size() != 1)
			throw new FunctionException("Path function must receive one argument");
		String path = arguments.get(0).toString();
		if (path.startsWith("/"))
			path = path.substring(1);
		return req.getContextPath() + "/" + path;
	}

}
