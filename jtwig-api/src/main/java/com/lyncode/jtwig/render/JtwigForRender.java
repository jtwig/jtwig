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
package com.lyncode.jtwig.render;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.tree.JtwigFor;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class JtwigForRender extends JtwigRender<JtwigFor> {

	public JtwigForRender(Map<String, Object> model, JtwigFor e) {
		super(model, e);
	}

	/* (non-Javadoc)
	 * @see com.lyncode.jtwig.render.JtwigRender#render()
	 */
	@Override
	public String render() throws JtwigRenderException {
		String result = "";
		Object obj = this.resolveExpression(this.getElement().getContainer().getVariable());
		if (obj instanceof List<?>) {
			List<?> l = (List<?>) obj;
			for (Object ob : l) {
				Map<String, Object> newMap = new TreeMap<String, Object>(this.getModel());
				newMap.put(this.getElement().getVariableName(), ob);
				result += new JtwigContentRender(newMap, getElement()).render();
			}
			return result;
		} else throw new JtwigRenderException(this.getElement().getContainer().getVariable() + " isn't a List");
	}

}
