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
package com.lyncode.jtwig.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;

import com.lyncode.jtwig.exceptions.FunctionException;
import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.functions.Function;
import com.lyncode.jtwig.tree.JtwigFunction;
import com.lyncode.jtwig.tree.JtwigValue;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class JtwigFunctionRender extends JtwigRender<JtwigFunction> {

	public JtwigFunctionRender(Map<String, Object> model, JtwigFunction e) {
		super(model, e);
	}

	/* (non-Javadoc)
	 * @see com.lyncode.jtwig.render.JtwigRender#render()
	 */
	@Override
	public String render() throws JtwigRenderException {
		List<String> params = new ArrayList<String>();
		for (JtwigValue v : this.getElement().getParameters())
			params.add(v.renderer(this.getModel()).render());
		
		String name = WordUtils.capitalizeFully(this.getElement().getFunctionName().replace('-', ' ')).replaceAll(" ", "");
		String functionName = Function.class.getPackage().getName() + "." + name;
		
		try {
			Class<?> clazz = this.getClass().getClassLoader().loadClass(functionName);
			Object obj = clazz.newInstance();
			if (obj instanceof Function) {
				return ((Function) obj).apply(params);
			} else throw new JtwigRenderException("Function "+this.getElement().getFunctionName()+" doesn't exist");
		} catch (ClassNotFoundException e) {
			throw new JtwigRenderException("Function "+this.getElement().getFunctionName()+" doesn't exist", e);
		} catch (FunctionException e) {
			throw new JtwigRenderException("Function "+this.getElement().getFunctionName()+" error", e);
		} catch (InstantiationException e) {
			throw new JtwigRenderException("Function "+this.getElement().getFunctionName()+" doesn't exist", e);
		} catch (IllegalAccessException e) {
			throw new JtwigRenderException("Function "+this.getElement().getFunctionName()+" doesn't exist", e);
		}
		
	}

}
