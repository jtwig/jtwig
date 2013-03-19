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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lyncode.jtwig.exceptions.JtwigParsingException;
import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.manager.JtwigResource;
import com.lyncode.jtwig.render.Renderable;
import com.lyncode.jtwig.render.Resolvable;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class ObjectList extends ArrayList<Object> implements Renderable, Resolvable {
	private static final long serialVersionUID = 6581105515775675565L;
	
	public ObjectList () {
		super();
	}
	
	public ObjectList(Integer pop, Integer pop2) {
		for (int i=pop;i<=pop2;i++) {
			this.add(i);
		}
	}

	public String render(HttpServletRequest req, Map<String, Object> model) throws JtwigRenderException {
		String result = "";
		for (Object obj : this) {
			if (obj instanceof Renderable) {
				result += ((Renderable) obj).render(req, model);
			} else if (obj instanceof String) {
				result += (String) obj;
			}
		}
		return result;
	}

	public void replace(Include icl, ObjectList parent) {
		boolean replaced = false;
		for (int i = 0;i<this.size() && !replaced;i++) {
			if (this.get(i) instanceof Include) {
				if (((Include) this.get(i)).getPath().equals(icl.getPath())) {
					this.set(i, parent);
					replaced = true;
				}
			}
		}
	}

	public boolean replace(Block block) {
		boolean replaced = false;
		for (int i = 0;i<this.size() && !replaced;i++) {
			if (this.get(i) instanceof Block) {
				if (((Block) this.get(i)).getName().equals(block.getName())) {
					ObjectList l = new ObjectList();
					l.addAll(block);
					this.set(i, l);
					replaced = true;
				}
			} else if (this.get(i) instanceof Resolvable) {
				((Resolvable) this.get(i)).replace(block);
			}
		}
		return replaced;
	}
	
	
	public String toString () {
		return "CONTAINER ("+this.size()+")";
	}

	@Override
	public void resolve(JtwigResource parent) throws IOException, JtwigParsingException {
		boolean extend = false;
		if (!this.isEmpty()) {
			if (this.get(0) instanceof Extends) {
				// This is an extend template (resolve it)
				ObjectList result = ((Extends) this.get(0)).resolve(parent, this);
				this.clear();
				this.addAll(result);
				extend = true;
			}
		}
		
		if (!extend) {
			for (int i=0;i<this.size();i++) {
				Object obj = this.get(i);
				if (obj instanceof Include) {
					this.set(i, ((Include) obj).resolve(parent));
				} else if (obj instanceof Resolvable) {
					((Resolvable) obj).resolve(parent);
				}
			}
		}
	}

	@Override
	public String render(Map<String, Object> model) throws JtwigRenderException {
		String result = "";
		for (Object obj : this) {
			if (obj instanceof Renderable) {
				result += ((Renderable) obj).render(model);
			} else if (obj instanceof String) {
				result += (String) obj;
			}
		}
		return result;
	}
}
