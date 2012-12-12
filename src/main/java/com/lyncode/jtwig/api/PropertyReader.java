package com.lyncode.jtwig.api;

public interface PropertyReader {
	String getString (String key, String defaultValue);
	String getString (String module, String key, String defaultValue);
	
	int getInt (String key, int defaultValue);
	int getInt (String module, String key, int defaultValue);
	
	boolean getBoolean (String key, boolean defaultValue);
	boolean getBoolean (String module, String key, boolean defaultValue);
}
