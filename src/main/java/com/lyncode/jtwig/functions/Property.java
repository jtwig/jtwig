package com.lyncode.jtwig.functions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lyncode.jtwig.api.PropertyReader;
import com.lyncode.jtwig.exceptions.FunctionException;

public class Property extends Function {
	@Autowired(required=false) PropertyReader config;
	
	@Override
	public Object apply(List<Object> arguments)
			throws FunctionException {
		if (config == null)
			throw new FunctionException("Function property must be active");
		if (arguments.size() == 1) {
			// get by key
			return config.getString((String)arguments.get(0), (String) null);
		} else if (arguments.size() == 2) {
			return config.getString((String)arguments.get(0), (String) arguments.get(1), (String) null);
		} else throw new FunctionException("Function property must receive at least one parameter");
	}

}
