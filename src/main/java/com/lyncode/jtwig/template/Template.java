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
package com.lyncode.jtwig.template;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.lyncode.jtwig.elements.ObjectList;
import com.lyncode.jtwig.exceptions.JtwigParsingException;
import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.exceptions.TemplateBuildException;
import com.lyncode.jtwig.manager.FileSystemResource;
import com.lyncode.jtwig.manager.JtwigResource;
import com.lyncode.jtwig.manager.ServletContextResource;
import com.lyncode.jtwig.parser.JtwigExtendedParser;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class Template {
	public static String read (String file, Map<String, Object> model) throws JtwigParsingException, IOException, JtwigRenderException {
		JtwigResource resource = new FileSystemResource(file);
		ObjectList list = JtwigExtendedParser.parse(resource.retrieve());
		list.resolve(resource);
		return list.render(model);
	}
	
	private ObjectList resolved;
	
	public Template (ServletContext servletContext, String filename) throws TemplateBuildException  {
		try {
			JtwigResource rsc = new ServletContextResource(servletContext).getRelativeResource(filename);
			resolved = JtwigExtendedParser.parse(rsc.retrieve());
			resolved.resolve(rsc);
		} catch (IOException e) {
			throw new TemplateBuildException(e);
		} catch (JtwigParsingException e) {
			throw new TemplateBuildException(e);
		}
	}
		
	public void process (HttpServletRequest request, Map<String, Object> model, OutputStream out) throws JtwigRenderException {
		try {
			out.write(this.resolved.render(request, model).getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new JtwigRenderException(e);
		}
	}
}
