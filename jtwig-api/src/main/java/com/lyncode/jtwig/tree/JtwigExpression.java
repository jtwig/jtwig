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
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class JtwigExpression extends JtwigElement {
	private static Logger log = LogManager.getLogger(JtwigExpression.class);
	private JtwigValue value;
	private List<JtwigFilter> filters;
	
	public JtwigExpression(JtwigValue value) {
		super();
		this.value = value;
		this.filters = new ArrayList<JtwigFilter>();
		log.debug("Expression");
	}

	public JtwigValue getValue() {
		return value;
	}

	public List<JtwigFilter> getFilters() {
		return filters;
	}
	
	public boolean add (JtwigFilter filter) {
		log.debug("Adding filter "+filter.getFilterName());
		this.filters.add(filter);
		return true;
	}
}
