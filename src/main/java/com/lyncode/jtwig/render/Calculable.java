package com.lyncode.jtwig.render;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lyncode.jtwig.exceptions.JtwigRenderException;

public interface Calculable {
	public Object calculate (HttpServletRequest req, Map<String, Object> values) throws JtwigRenderException;
}
