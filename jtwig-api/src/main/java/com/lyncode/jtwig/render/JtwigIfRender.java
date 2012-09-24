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

import java.util.Map;

import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.tree.JtwigIf;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class JtwigIfRender extends JtwigRender<JtwigIf> {

	public JtwigIfRender(Map<String, Object> model, JtwigIf e) {
		super(model, e);
	}

	/* (non-Javadoc)
	 * @see com.lyncode.jtwig.render.JtwigRender#render()
	 */
	@Override
	public String render() throws JtwigRenderException {
		String testResult;
		try {
			testResult = this.getElement().getName().renderer(getModel()).render().toLowerCase();
		} catch (JtwigRenderException e) {
			testResult = "";
		}
		if (testResult.equals("true")) {
			return (new JtwigContentRender(getModel(), getElement())).render();
		} else {
			if (getElement().getElseContent() != null)
				return (new JtwigContentRender(getModel(), getElement().getElseContent())).render();
			return "";
		}
	}

}
