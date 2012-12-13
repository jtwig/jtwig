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
	
	private ObjectList first;
	private ObjectList last;
	
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
	
	public ObjectList getFirst() {
		return first;
	}

	public boolean setFirst(ObjectList first) {
		this.first = first;
		return true;
	}

	public ObjectList getLast() {
		return last;
	}

	public boolean setLast(ObjectList last) {
		this.last = last;
		return true;
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
		else if (!(values instanceof List<?>)) {
			forValues = new ArrayList<Object>();
			forValues.add(values);
		} else forValues = (List<Object>) values;
		
		
		for (int i = 0;i<forValues.size();i++) {
			Object val = forValues.get(i);
			Map<String, Object> newModel = new TreeMap<String, Object>();
			newModel.putAll(model);
			newModel.put(variable, val);
			
			if (i == 0 && this.first != null) {
				for (Object obj : this.first) {
					if (obj instanceof Invoke) {
						((Invoke) obj).invoke(req, newModel);
					} else {
						if (obj instanceof Renderable) {
							result += ((Renderable) obj).render(req, newModel, manager);
						} else if (obj instanceof String) {
							result += (String) obj;
						} else throw new JtwigRenderException("Unable to render object "+obj.toString());
					}
				}
			} else if (i == (forValues.size() - 1) && this.last != null) {
				for (Object obj : this.last) {
					if (obj instanceof Invoke) {
						((Invoke) obj).invoke(req, newModel);
					} else {
						if (obj instanceof Renderable) {
							result += ((Renderable) obj).render(req, newModel, manager);
						} else if (obj instanceof String) {
							result += (String) obj;
						} else throw new JtwigRenderException("Unable to render object "+obj.toString());
					}
				}
			} else {
				for (Object obj : this) {
					if (obj instanceof Invoke) {
						((Invoke) obj).invoke(req, newModel);
					} else {
						if (obj instanceof Renderable) {
							result += ((Renderable) obj).render(req, newModel, manager);
						} else if (obj instanceof String) {
							result += (String) obj;
						} else throw new JtwigRenderException("Unable to render object "+obj.toString());
					}
				}
			}
			
		}
		return result;
	}

	public String toString () {
		return "FOR: "+variable+", VALUE: "+value.toString();
	}
}
