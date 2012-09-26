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
package com.lyncode.jtwig.elements;

import java.util.Map;
import java.util.TreeMap;

import com.lyncode.jtwig.expression.JtwigExpressionEvaluator;
import com.lyncode.jtwig.manager.ResourceManager;
import com.lyncode.jtwig.render.Renderable;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class For extends ObjectList {
	private static final long serialVersionUID = 4648580478468941354L;
	private String variable;
	private Object value;
	
	public For (String variable, Object value) {
		this.variable = variable;
		this.value = value;
	}

	public String getVariable() {
		return variable;
	}

	public Object getValue() {
		return value;
	}
	
	public String render(Map<String, Object> model, ResourceManager manager) {
		String result = "";
		JtwigExpressionEvaluator eval = new JtwigExpressionEvaluator(model);
		if (this.value instanceof ObjectList) {
			
		} else if (this.value instanceof Variable) {
			
		} else if (this.)
		
		for (Object obj : this) {
			Map<String, Object> newModel = new TreeMap<String, Object>();
			newModel.putAll(model);
			if (obj instanceof Renderable) {
				result += ((Renderable) obj).render(model, manager);
			} else if (obj instanceof String) {
				result += (String) obj;
			}
		}
		return result;
	}
	
}
