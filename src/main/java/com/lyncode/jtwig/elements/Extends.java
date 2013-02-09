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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.jtwig.exceptions.JtwigParsingException;
import com.lyncode.jtwig.manager.JtwigResource;
import com.lyncode.jtwig.parser.JtwigExtendedParser;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class Extends {
	private static Logger log = LogManager.getLogger(Extends.class);
	private String path;

	public Extends(String path) {
		super();
		this.path = path;
		log.debug(this);
	}

	public String getPath() {
		return path;
	}
	

	public String toString () {
		return "Extends: "+path;
	}

	public ObjectList resolve(JtwigResource parent, ObjectList blocks) throws IOException, JtwigParsingException {
		JtwigResource resource = parent.getRelativeResource(this.getPath());
		ObjectList list = JtwigExtendedParser.parse(resource.retrieve());
		// Now replace the blocks
		for (int i=1;i<blocks.size();i++)
			((Block)blocks.get(i)).resolve(resource);
		
		for (int i=1;i<blocks.size();i++) {
			if (list.replace((Block)blocks.get(i)))
				log.debug("Block "+((Block)blocks.get(i)).getName()+" replaced");
		}
		
		list.resolve(resource);
		return list;
	}
}
