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
package com.lyncode.jtwig.mvc;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

/**
 * @author "Joao Melo <jmelo@lyncode.com>"
 *
 */
public class JtwigViewResolver extends AbstractTemplateViewResolver  {
	@Autowired DefaultThemeResolver theme;
	
	private boolean cached;
	
	public boolean isCached () {
		return cached;
	}
	
	public void setCached (boolean b) {
		cached = b;
	}
	
	public JtwigViewResolver () {
		setViewClass(requiredViewClass());
	}

	@Override
	protected Class<?> requiredViewClass() {
		return JtwigView.class;
	}
	
	@Override
	protected String getPrefix() {
		if (theme == null)
			return super.getPrefix();
		else
			return super.getPrefix() + theme.getTheme() + File.separator;
	}

	private String encoding;

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getEncoding() {
		return encoding;
	}
}
