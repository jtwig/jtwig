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
package com.lyncode.jtwig.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class JtwigContent extends JtwigElement {
	private List<JtwigElement> childs;
	
	public JtwigContent () {
		childs = new ArrayList<JtwigElement>();
	}
	
	public boolean add (JtwigElement t) {
		childs.add(t);
		return true;
	}
	
	public List<JtwigElement> getChilds () {
		return childs;
	}

	public void replace(JtwigInclude icl, JtwigRoot parent) {
		JtwigContent content = new JtwigContent();
		for (JtwigElement e : parent.getChilds())
			content.add(e);
		Collections.replaceAll(childs, icl, content);
	}

	public void replace(JtwigElement icl, JtwigContent content) {
		Collections.replaceAll(childs, icl, content);
	}
}
