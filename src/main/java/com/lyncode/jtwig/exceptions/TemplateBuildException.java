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
package com.lyncode.jtwig.exceptions;

/**
 * @author "Joao Melo <jmelo@lyncode.com>"
 *
 */
public class TemplateBuildException extends Exception {

	private static final long serialVersionUID = 5610926121135477771L;

	public TemplateBuildException() {
		super();
	}

	public TemplateBuildException(String message, Throwable cause) {
		super(message, cause);
	}

	public TemplateBuildException(String message) {
		super(message);
	}

	public TemplateBuildException(Throwable cause) {
		super(cause);
	}

}
