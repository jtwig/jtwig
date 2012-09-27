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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.manager.ResourceManager;
import com.lyncode.jtwig.render.Calculable;
import com.lyncode.jtwig.render.Renderable;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class For extends ObjectList {
	private static Logger log = LogManager.getLogger(For.class);
	private static final long serialVersionUID = 4648580478468941354L;
	private String variable;
	private Object value;
	
	public For (String variable, Object value) {
		this.variable = variable;
		this.value = value;
		log.debug(this);
	}

	public String getVariable() {
		return variable;
	}

	public Object getValue() {
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public String render(Map<String, Object> model, ResourceManager manager) throws JtwigRenderException {
		String result = "";
		Object values = null;
		if (this.value instanceof Calculable) {
			values = ((Calculable) this.value).calculate(model);
		} else values = this.value;
		
		List<Object> forValues = null;
		
		if (values == null) forValues = new ObjectList();
		else if (!(values instanceof List<?>)) {
			forValues = new ArrayList<Object>();
			forValues.add(values);
		} else forValues = (List<Object>) values;
		
		for (Object val : forValues) {
			Map<String, Object> newModel = new TreeMap<String, Object>();
			newModel.putAll(model);
			newModel.put(variable, val);
			for (Object obj : this) {
				if (obj instanceof Renderable) {
					result += ((Renderable) obj).render(newModel, manager);
				} else if (obj instanceof String) {
					result += (String) obj;
				} else throw new JtwigRenderException("Unable to render object "+obj.toString());
			}
		}
		return result;
	}

	public String toString () {
		return "FOR: "+variable+", VALUE: "+value.toString();
	}
}
