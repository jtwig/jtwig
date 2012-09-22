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

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.tree.JtwigElement;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public abstract class JtwigRender<T extends JtwigElement> {
	private Map<String, Object> model;
	private EvaluationContext context;
	private ExpressionParser parser;
	private T e;
	
	public JtwigRender (Map<String, Object> model, T e) {
		this.model = model;
		context = new StandardEvaluationContext(model);
		parser = new SpelExpressionParser();
		this.e = e;
	}

	protected Object resolveExpression (String expression) {
		return parser.parseExpression(expression).getValue(context);
	}
	
	public T getElement () {
		return e;
	}
	
	public Map<String, Object> getModel () {
		return this.model;
	}
	
	public abstract String render () throws JtwigRenderException;
}
