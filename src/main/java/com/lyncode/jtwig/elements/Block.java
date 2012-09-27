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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.manager.ResourceManager;


/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class Block extends ObjectList {
	private static Logger log = LogManager.getLogger(Block.class);
	private static final long serialVersionUID = -3386153290112843228L;
	private String name;

	public Block(String name) {
		super();
		this.name = name;
		log.debug(this);
	}

	public String getName() {
		return name;
	}

	public String render(Map<String, Object> model, ResourceManager manager) throws JtwigRenderException {
		this.setBlock();
		return super.render(model, manager);
	}
	
	public boolean equals (Object obj) {
		if (obj instanceof Block)
			return ((Block) obj).getName().equals(name);
		return false;
	}
	
	public String toString () {
		return "BLOCK: "+name;
	}
}
