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

import java.util.List;
import java.util.Map;

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
public class If extends ObjectList {
	private static Logger log = LogManager.getLogger(If.class);
	private static final long serialVersionUID = -8676097057249972628L;
	private Object value;
	private ObjectList elseContent;
	public If(Object value, ObjectList elseContent) {
		super();
		this.value = value;
		this.elseContent = elseContent;
		log.debug(this);
	}
	
	public If(Object value) {
		super();
		this.elseContent = null;
		this.value = value;
		log.debug(this);
	}
	
	public Object getValue() {
		return value;
	}
	public ObjectList getElseContent() {
		return elseContent;
	}
	
	public boolean hasElse () {
		return this.elseContent != null;
	}
	
	public boolean setElse (ObjectList l) {
		this.elseContent = l;
		return true;
	}
	

	public String render(HttpServletRequest req, Map<String, Object> model, ResourceManager manager) throws JtwigRenderException {
		String result = "";
		Object values = null;
		if (this.value instanceof Calculable) {
			values = ((Calculable) this.value).calculate(req, model);
		} else values = this.value;
		
		boolean test = false;
		
		if (values != null) {
			if (values instanceof List) {
				test = (((List<?>)values).size() > 0);
			} else if (values instanceof Boolean) {
				test = ((Boolean) values);
			} else test = true; // Non null object
		}
		if (test) {
			log.debug("Rendering if content");
			for (Object obj : this) {
				if (obj instanceof Invoke) {
					((Invoke) obj).invoke(req, model);
				} else {
					if (obj instanceof Renderable) {
						result += ((Renderable) obj).render(req, model, manager);
					} else if (obj instanceof String) {
						result += (String) obj;
					} else throw new JtwigRenderException("Unable to render object "+obj.toString());
				}
			}
		} else {
			log.debug("Rendering else content (if exists)");
			if (this.hasElse()) {
				for (Object obj : this.getElseContent()) {
					if (obj instanceof Invoke) {
						((Invoke) obj).invoke(req, model);
					} else {
						if (obj instanceof Renderable) {
							result += ((Renderable) obj).render(req, model, manager);
						} else if (obj instanceof String) {
							result += (String) obj;
						} else throw new JtwigRenderException("Unable to render object "+obj.toString());
					}
				}
			}
		}
		log.debug("Render: "+result);
		return result;
	}
	

	public String toString () {
		if (this.hasElse())
			return "IF: "+value+" with ELSE";
		else
			return "IF: "+value+" without ELSE";
	}
}
