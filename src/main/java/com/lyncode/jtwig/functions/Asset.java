package com.lyncode.jtwig.functions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lyncode.jtwig.api.AssetResolver;
import com.lyncode.jtwig.exceptions.FunctionException;

public class Asset extends Function {
	@Autowired AssetResolver assetResolver;
	
	@Override
	public Object apply(List<Object> arguments) throws FunctionException {
		if (arguments.size() != 1)
			throw new FunctionException("Function "+this.getClass().getSimpleName()+" requires only one argument");
		return assetResolver.getPath((String)arguments.get(0));
	}

}
