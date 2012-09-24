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

import java.util.List;

import com.lyncode.jtwig.exceptions.FunctionException;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class Not extends Function {

	/* (non-Javadoc)
	 * @see com.lyncode.jtwig.functions.Function#apply(java.util.List)
	 */
	@Override
	public String apply(List<String> arguments) throws FunctionException {
		if (arguments.size() != 1)
			throw new FunctionException("Not function must receive one argument");
		return new Boolean(!arguments.get(0).equals(new Boolean(true).toString())).toString();
	}

}
