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
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.parboiled.common.FileUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.lyncode.jtwig.elements.Block;
import com.lyncode.jtwig.elements.Extends;
import com.lyncode.jtwig.elements.Include;
import com.lyncode.jtwig.elements.ObjectList;
import com.lyncode.jtwig.exceptions.JtwigParsingException;
import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.exceptions.TemplateBuildException;
import com.lyncode.jtwig.manager.ResourceManager;
import com.lyncode.jtwig.manager.ServletContextResourceManager;
import com.lyncode.jtwig.mvc.AfterRenderRunnableBeans;
import com.lyncode.jtwig.parser.JtwigExtendedParser;

/**
 * @author "João Melo <jmelo@lyncode.com>"
 *
 */
public class Template {
	private ServletContext servletContext;
	private ResourceManager resources;
	private ObjectList resolved;
	
	public Template (ServletContext servletContext, String filename) throws TemplateBuildException  {
		try {
			this.servletContext = servletContext;
			this.resources = new ServletContextResourceManager(servletContext, filename);
			resolved = this.resolve();
		} catch (TemplateBuildException e) {
			throw new TemplateBuildException(e);
		}
	}
	
	private Template loadTemplate (String relativePath) throws TemplateBuildException {
		try {
			return new Template(servletContext, resources.getFile(relativePath));
		} catch (IOException e) {
			throw new TemplateBuildException(e);
		}
	}

	private void replaceIncludes (ObjectList content) throws TemplateBuildException {
		Collection<Object> includes = Collections2.filter(content, new Predicate<Object>() {
			public boolean apply(Object input) {
				return (input instanceof Include);
			}
		});
		
		for (Object inc : includes) {
			Include icl = (Include) inc;
			Template t = this.loadTemplate(icl.getPath());
			ObjectList parent = t.resolve();
			content.replace(icl, parent);
		}
		
		Collection<Object> contents = Collections2.filter(content, new Predicate<Object>() {
			public boolean apply(Object input) {
				return (input instanceof ObjectList);
			}
		});
		
		for (Object ct : contents) {
			ObjectList icl = (ObjectList) ct;
			this.replaceIncludes(icl);
		}
	}
	
	private ObjectList resolve () throws TemplateBuildException {
		byte[] readed;
		try {
			readed = FileUtils.readAllBytes(resources.getResource());
		} catch (IOException e1) {
			throw new TemplateBuildException(e1);
		}
		String input = new String(readed);
		ObjectList root;
		try {
			root = JtwigExtendedParser.parse(input);
		} catch (JtwigParsingException e) {
			throw new TemplateBuildException(e);
		}
		if (!root.isEmpty()) {
			// Search the tree for Includes and replace them
			this.replaceIncludes(root);
			
			// Search the root tree for Blocks and replace them
			if (root.get(0) instanceof Extends) {
				// Extension template
				Extends ext = (Extends) root.get(0);
				Template t = this.loadTemplate(ext.getPath());
				
				ObjectList parent = t.resolve();
				
				Collection<Object> masterBlocks = Collections2.filter(parent, new Predicate<Object>() {
					public boolean apply(Object input) {
						return (input instanceof Block);
					}
				});
				
				Collection<Object> thisBlocks = Collections2.filter(root, new Predicate<Object>() {
					public boolean apply(Object input) {
						return (input instanceof Block);
					}
				});
				
				if (thisBlocks.size() > root.size() + 1)
					throw new TemplateBuildException(this.resources.getPath() + " template with unexpected constructions");
				
				for (Object elem : thisBlocks) {
					Block block = (Block) elem;
					if (!masterBlocks.contains(block))
						throw new TemplateBuildException("Undefined block "+block.getName());
					
					parent.replace(block);
				}
				
				return parent;
			}
		}
		
		return root;
	}
	
	public void process (HttpServletRequest request, Map<String, Object> model, OutputStream out) throws JtwigRenderException {
		try {
			out.write(this.resolved.render(request, model, resources).getBytes());
			out.flush();
			out.close();
			this.runEndingTasks(request);
		} catch (IOException e) {
			throw new JtwigRenderException(e);
		}
	}

	private void runEndingTasks(HttpServletRequest request) {
		ApplicationContext a = RequestContextUtils.getWebApplicationContext(request);
		try {
			AfterRenderRunnableBeans beans = a.getBean(AfterRenderRunnableBeans.class);
			if (beans != null) {
				for (String id : beans.getIds()) {
					Object o = a.getBean(id);
					if (o instanceof Runnable)
						((Runnable) o).run();
				}
			}
		} catch (NoSuchBeanDefinitionException e) {
			// nothing to do
		}
	}
}
