package com.lyncode.jtwig.render;

import java.util.Map;

import com.lyncode.jtwig.exceptions.JtwigRenderException;

public interface Calculable {
	public Object calculate (Map<String, Object> values) throws JtwigRenderException;
}
