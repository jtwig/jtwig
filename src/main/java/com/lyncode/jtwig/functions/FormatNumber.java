/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lyncode.jtwig.functions;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import com.lyncode.jtwig.exceptions.FunctionException;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class FormatNumber extends Function {

	/* (non-Javadoc)
	 * @see com.lyncode.jtwig.functions.Function#apply(java.util.List)
	 */
	@Override
	public Object apply(List<Object> arguments) throws FunctionException {
		if (arguments.size() != 1)
			throw new FunctionException("FormatNumber function must receive one argument");
		Object argument = arguments.get(0);
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator('.');
		DecimalFormat formatter = new DecimalFormat("#,###", symbols);
		formatter.setGroupingUsed(true);
		
		return formatter.format(argument);
	}

}
