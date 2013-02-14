package com.lyncode.jtwig.manager;

import java.io.File;
import java.io.IOException;

import org.parboiled.common.FileUtils;

public class FileSystemResource implements JtwigResource {
	private File current;

	public FileSystemResource (String directory) {
		current = new File(directory);
	}
	public FileSystemResource (File directory) {
		current = directory;
	}
	
	@Override
	public String retrieve() throws IOException {
		if (current.exists() && current.isFile())
			return FileUtils.readAllText(current);
		else return null;
	}

	@Override
	public JtwigResource getRelativeResource(String relativeToCurrent)
			throws IOException {
		if (current.isFile()) return new FileSystemResource(new File(current.getParent(), relativeToCurrent));
		else return new FileSystemResource(new File(current, relativeToCurrent)); 
	}
	

}
