package com.lyncode.jtwig.functions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.lyncode.jtwig.exceptions.FunctionException;

public class Ratio extends Function {

	@Autowired HttpServletRequest req;
	
	@Override
	public Object apply(List<Object> arguments)
			throws FunctionException {
		if (arguments.size() != 2)
			throw new FunctionException("Ratio function must receive two arguments");
		Object first = arguments.get(0);
		Object second = arguments.get(1);
		if ((second instanceof Number) && (first instanceof Number))
		{
			Number n1 = (Number) first;
			Number n2 = (Number) second;
			
			float nu1 = (float) n1.intValue();
			float nu2 = (float) n2.intValue();
			
			return (float) (nu1/nu2);
		} else throw new FunctionException("Ratio function must receive integers as arguments");
	}

}
