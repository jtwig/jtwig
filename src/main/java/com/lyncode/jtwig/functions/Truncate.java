package com.lyncode.jtwig.functions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.lyncode.jtwig.exceptions.FunctionException;

public class Truncate extends Function {

	@Autowired HttpServletRequest req;
	
	@Override
	public Object apply(List<Object> arguments)
			throws FunctionException {
		if (arguments.size() != 2)
			throw new FunctionException("Truncate function must receive two arguments");
		String first = arguments.get(0).toString();
		Object second = arguments.get(1);
		if (second instanceof Number)
		{
			Number n = (Number) second;
			if (first.length() > n.intValue()) {
				return first.substring(0, n.intValue()-3) + "...";
			} else return first;
		} else throw new FunctionException("Truncate function must receive an integer as second argument");
	}

}
