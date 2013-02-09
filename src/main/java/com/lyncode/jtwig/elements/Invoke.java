package com.lyncode.jtwig.elements;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.lyncode.jtwig.api.InvokeResolver;
import com.lyncode.jtwig.exceptions.JtwigRenderException;
import com.lyncode.jtwig.render.Renderable;

public class Invoke implements Renderable {
	private static Logger log = LogManager.getLogger(Invoke.class);
	
	private String className;
	private String methodName;
	private Map<String, Object> parameters;
	public Invoke(String className, String methodName) {
		super();
		this.className = className;
		this.methodName = methodName;
		this.parameters = new TreeMap<String, Object>();
	}
	public String getClassName() {
		return className;
	}
	public String getMethodName() {
		return methodName;
	}
	public Map<String, Object> getParameters() {
		return parameters;
	}
	public boolean add(String pop, Object pop2) {
		this.parameters.put(pop, pop2);
		return true;
	}
	public void invoke(HttpServletRequest req, Map<String, Object> model) {
		Object obj;
		log.debug("Invoked method "+this.methodName+" from class "+this.className);

		WebApplicationContext context = RequestContextUtils.getWebApplicationContext(req);
		InvokeResolver res = null;
		
		try {
			res = context.getBean(InvokeResolver.class);
		} catch (Exception ex) {
			log.debug(ex.getMessage(), ex);
		}
			
		try {
			if (res != null) obj = res.resolve(className);
			else obj = this.getClass().getClassLoader().loadClass(className).newInstance();
			
			context.getAutowireCapableBeanFactory().autowireBean(obj);
			for (Method m : obj.getClass().getMethods()) {
				if (m.getName().toLowerCase().equals(this.methodName.toLowerCase())) {
					List<Object> arguments = new ArrayList<Object>();
					if (m.getParameterTypes().length > 0)
						arguments.add(model);
					
					if (m.getParameterTypes().length > 1)
						arguments.add(this.parameters);
					
					m.invoke(obj, arguments.toArray());
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
	}
	
	@Override
	public String render(HttpServletRequest req, Map<String, Object> model) throws JtwigRenderException {
		this.invoke(req, model);
		return "";
	}
	
	
	
	
}
