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

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.manager.ResourceManager;
import com.lyncode.jtwig.render.Calculable;

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
	public String render(HttpServletRequest req, Map<String, Object> model, ResourceManager manager) throws JtwigRenderException {
		String result = "";
		Object values = null;
		if (this.value instanceof Calculable) {
			values = ((Calculable) this.value).calculate(req, model);
		} else values = this.value;
		
		List<Object> forValues = null;
		
		if (values == null) forValues = new ObjectList();
		else if (values instanceof List<?>) {
			forValues = (List<Object>) values;
		} else if (values.getClass().isArray()) {
			Object[] list = (Object[]) values;
			forValues = new ArrayList<Object>();
			for (int i=0;i<list.length;i++)
				forValues.add(list[i]);
		} else {
			forValues = new ArrayList<Object>();
			forValues.add(values);
		}
		
		
		for (int i = 0;i<forValues.size();i++) {
			Object val = forValues.get(i);
			Map<String, Object> newModel = new TreeMap<String, Object>();
			newModel.putAll(model);
			newModel.put(variable, val);
			newModel.put("position", i);
			newModel.put("first", i == 0);
			newModel.put("last", (i + 1) == forValues.size());
			result += super.render(req, newModel, manager);
		}
		return result;
	}

	public String toString () {
		return "FOR: "+variable+", VALUE: "+value.toString();
	}
}
