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
import java.util.Map;

import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.manager.ResourceManager;
import com.lyncode.jtwig.render.Renderable;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class ObjectList extends ArrayList<Object> implements Renderable {
	private static final long serialVersionUID = 6581105515775675565L;
	
	public ObjectList () {
		super();
	}

	private boolean block = false;
	
	public boolean hasBlock () {
		return block;
	}
	
	public void setBlock () {
		block = true;
	}
	
	public String render(Map<String, Object> model, ResourceManager manager) throws JtwigRenderException {
		String result = "";
		for (Object obj : this) {
			if (this.hasBlock()) {
				if (obj instanceof Block)
					throw new JtwigRenderException("Cannot have nested blocks");
				else if (obj instanceof ObjectList)
					((ObjectList) obj).setBlock();
			}
			if (obj instanceof Renderable) {
				result += ((Renderable) obj).render(model, manager);
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

	public void replace(Block block) {
		boolean replaced = false;
		for (int i = 0;i<this.size() && !replaced;i++) {
			if (this.get(i) instanceof Block) {
				if (((Block) this.get(i)).getName().equals(block.getName())) {
					this.set(i, block);
					replaced = true;
				}
			}
		}
	}
	
	public String toString () {
		return "CONTAINER ("+this.size()+")";
	}
}
