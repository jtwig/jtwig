package com.lyncode.jtwig.mvc;

import java.util.regex.Pattern;

import com.lyncode.jtwig.api.InvokeResolver;

public class DefaultInvokeResolver implements InvokeResolver {
	private String path;
	
	
	public String getPath() {
		return path;
	}


	public void setPackage(String path) {
		this.path = path;
	}


	@Override
	public Object resolve(String name) {
		String parts[] = this.path.split(Pattern.quote(","));
		for (String part : parts) {
			if (!part.endsWith(".")) part += ".";
			try {
				return this.getClass().getClassLoader().loadClass(part + name).newInstance();
			} catch (Exception e) {
				// Nothing
			} 
		}
		try {
			return this.getClass().getClassLoader().loadClass(name).newInstance();
		} catch (Exception e1) {
			return null;
		} 
	}

}
