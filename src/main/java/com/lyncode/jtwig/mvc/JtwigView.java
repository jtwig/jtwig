package com.lyncode.jtwig.mvc;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.web.servlet.view.AbstractTemplateView;

import com.lyncode.jtwig.exceptions.JtwigParsingException;
import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.exceptions.TemplateBuildException;
import com.lyncode.jtwig.template.Template;

public class JtwigView extends AbstractTemplateView {
	private static Logger log = LogManager.getLogger(JtwigView.class);
	
    public static final String KEY_REQUEST = "Request";
    public static final String KEY_INCLUDE = "include_page";
    public static final String KEY_REQUEST_PARAMETERS = "RequestParameters";
    public static final String KEY_SESSION = "Session";
    
	private String encoding;

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	protected String getEncoding() {
		return this.encoding;
	}
	
	protected void initApplicationContext() throws BeansException {
		super.initApplicationContext();
		GenericServlet servlet = new GenericServletAdapter();
		try {
			servlet.init(new DelegatingServletConfig());
		} catch (ServletException ex) {
			throw new BeanInitializationException("Initialization of GenericServlet adapter failed", ex);
		}
	}
	
	@Override
	protected void renderMergedTemplateModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		exposeModelAsRequestAttributes(model, request);

		if (log.isDebugEnabled()) {
			log.debug("Rendering Jtwig template [" + getUrl() + "] in JtwigView '" + getBeanName() + "'");
		}
		
		processTemplate(getTemplate(request.getServletContext(), getUrl()), request, model, response);
	}
	
	// private static Map<String, Template> templates = new HashMap<String, Template>();
	
	private static Template getTemplate (ServletContext servletContext, String url) throws IOException, JtwigParsingException, TemplateBuildException {
		return new Template(servletContext, url);
		/*if (!templates.containsKey(url)) {
			templates.put(url, new Template(servletContext, url));
		}
		return templates.get(url);*/
	}
	
	
	private void processTemplate(Template template, HttpServletRequest request, Map<String, Object> model,
			HttpServletResponse response) throws JtwigRenderException {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType(this.getContentType());
			template.process(request, model, response.getOutputStream());
		} catch (IOException e) {
			throw new JtwigRenderException(e);
		}
	}
	
	@SuppressWarnings("serial")
	private static class GenericServletAdapter extends GenericServlet {
		public void service(ServletRequest servletRequest, ServletResponse servletResponse) {
			// no-op
		}
	}
	
	private class DelegatingServletConfig implements ServletConfig {
		public String getServletName() {
			return JtwigView.this.getBeanName();
		}
		public ServletContext getServletContext() {
			return JtwigView.this.getServletContext();
		}
		public String getInitParameter(String paramName) {
			return null;
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Enumeration getInitParameterNames() {
			return Collections.enumeration(Collections.EMPTY_SET);
		}
	}
	
	
}
