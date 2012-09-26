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

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class If extends ObjectList {
	private static final long serialVersionUID = -8676097057249972628L;
	private Object value;
	private ObjectList elseContent;
	public If(Object value, ObjectList elseContent) {
		super();
		this.value = value;
		this.elseContent = elseContent;
	}
	
	public If(Object value) {
		super();
		this.value = value;
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
}
