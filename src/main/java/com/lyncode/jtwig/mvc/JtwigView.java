package com.lyncode.jtwig.mvc;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
import org.springframework.beans.factory.BeanCreationException;
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
		try {
			Theme theme = this.getWebApplicationContext().getBean(Theme.class);
			if (theme == null) model.put("theme", "");
			else model.put("theme", theme.getTheme());
		} catch (BeanCreationException e) {
			log.debug(e.getMessage(), e);
		}
		
		addRequestAttributes(request, model);

		if (log.isDebugEnabled()) {
			log.debug("Rendering Jtwig template [" + getUrl() + "] in JtwigView '" + getBeanName() + "'");
			log.debug("Model: "+showModel(model));
		}
		
		JtwigViewResolver viewResolver = this.getApplicationContext().getBean(JtwigViewResolver.class);
		if (!viewResolver.isCached()) {
			processTemplate(getTemplate(request.getServletContext(), getUrl()), request, model, response);
		} else {
			processTemplate(getCachedTemplate(request.getServletContext(), getUrl()), request, model, response);
		}
		
	}
	
	private static String showModel (Map<?, ?> model) {
		return showModel(model, 0);
	}
	
	private static String showModel (Map<?, ?> model, int spaces) {
		StringBuilder b = new StringBuilder();
		String padd = "";
		for (int i=0;i<(spaces*2);i++)
			padd += " ";
		for (Object n : model.keySet()) {
			b.append(padd);
			b.append("'"+n.toString()+"': ");
			Object obj = model.get(n);
			if (obj instanceof Map) {
				b.append("\n");
				b.append(showModel((Map<?, ?>)obj, spaces+1));
			} else {
				if (obj == null)
					b.append("null");
				else
					b.append("'"+obj.toString()+"'");
				b.append("\n");
			}
		}
		return b.toString();
	}
	
	
	private void addRequestAttributes(HttpServletRequest request,
			Map<String, Object> model) {
		Map<String, Object> parameters = new TreeMap<String, Object>();
		Map<String, String[]> m = request.getParameterMap();
		
		for (String k : m.keySet()) {
			if (m.get(k).length > 1)
				parameters.put(k, Arrays.asList(m.get(k)));
			else if (m.get(k).length == 1)
				parameters.put(k, m.get(k)[0]);
		}

		
		
		Map<String, Object> remote = new TreeMap<String, Object>();
		remote.put("address", request.getRemoteAddr());
		remote.put("host", request.getRemoteHost());
		remote.put("port", request.getRemotePort());
		remote.put("user", request.getRemoteUser());

		Map<String, Object> context = new TreeMap<String, Object>();
		context.put("path", request.getServletContext().getContextPath());
		// remote.put("parameter", request.getServletContext().getInitParameterNames());

		
		Map<String, Object> mm = new TreeMap<String, Object>();
		mm.put("parameter", parameters);
		mm.put("remote", remote);
		mm.put("context", context);
		
		mm.put("method", request.getMethod());
		mm.put("locale", request.getLocale().getLanguage());
		mm.put("uri", request.getRequestURI());
		mm.put("path", request.getPathInfo());
		mm.put("protocol", request.getProtocol());
		
		model.put("request", mm);
	}

	private static Map<String, Template> templates = new HashMap<String, Template>();

	private static Template getTemplate (ServletContext servletContext, String url) throws IOException, JtwigParsingException, TemplateBuildException {
		return new Template(servletContext, url);
	}

	private static Template getCachedTemplate (ServletContext servletContext, String url) throws IOException, JtwigParsingException, TemplateBuildException {
		if (!templates.containsKey(url)) {
			templates.put(url, new Template(servletContext, url));
		}
		return templates.get(url);
	}
	
	private void processTemplate(Template template, HttpServletRequest request, Map<String, Object> model,
			HttpServletResponse response) throws JtwigRenderException {
		try {
			
			response.setContentType(this.getContentType());
			response.setCharacterEncoding("UTF-8");
			
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
