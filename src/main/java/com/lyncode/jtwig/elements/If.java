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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.jtwig.exceptions.JtwigParsingException;
import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.manager.JtwigResource;
import com.lyncode.jtwig.render.Calculable;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class If extends ObjectList {
	private static Logger log = LogManager.getLogger(If.class);
	private static final long serialVersionUID = -8676097057249972628L;
	private Object value;
	private ObjectList elseContent;
	private List<If> elseifs;
	public If(Object value, ObjectList elseContent) {
		super();
		this.value = value;
		this.elseContent = elseContent;
		this.elseifs = new ArrayList<If>();
		log.debug(this);
	}
	
	public If(Object value) {
		super();
		this.elseContent = null;
		this.value = value;
		this.elseifs = new ArrayList<If>();
		log.debug(this);
	}
	
	public boolean addElseIf (If i) {
		this.elseifs.add(i);
		return true;
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
	
	private boolean isTrue (HttpServletRequest req, Map<String, Object> model) throws JtwigRenderException {
		Object values = null;
		if (this.value instanceof Calculable) {
			values = ((Calculable) this.value).calculate(req, model);
		} else values = this.value;
		return JtwigExpression.isTrue(values);
	}
	
	private String renderSuper (HttpServletRequest req, Map<String, Object> model) throws JtwigRenderException {
		return super.render(req, model);
	}

	public String render(HttpServletRequest req, Map<String, Object> model) throws JtwigRenderException {
		String result = "";
		if (this.isTrue(req, model)) {
			log.debug("Rendering if content");
			result += super.render(req, model);
		} else {
			boolean alreadyRunned = false;
			for (If i : this.elseifs) {
				if (i.isTrue(req, model)) {
					result += i.renderSuper(req, model);
					alreadyRunned = true;
					break;
				}
			}
			if (!alreadyRunned) {
				if (this.hasElse())
					result += this.getElseContent().render(req, model);
			}
		}
		return result;
	}
	

	@Override
	public void resolve(JtwigResource parent) throws IOException, JtwigParsingException {
		super.resolve(parent);
		if (this.elseContent != null) this.elseContent.resolve(parent);
		if (this.elseifs != null)
			for (If i : this.elseifs)
				i.resolve(parent);
	}
	
	public boolean replace(Block block) {
		boolean replaced = super.replace(block);
		replaced = replaced && this.elseContent.replace(block);
		for (If i : this.elseifs)
			replaced = replaced && i.replace(block);
		return replaced;
	}

	public String toString () {
		if (this.hasElse())
			return "IF: "+value+" with ELSE";
		else
			return "IF: "+value+" without ELSE";
	}
}
