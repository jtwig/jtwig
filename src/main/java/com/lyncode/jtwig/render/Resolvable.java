package com.lyncode.jtwig.render;

import java.io.IOException;

import com.lyncode.jtwig.elements.Block;
import com.lyncode.jtwig.exceptions.JtwigParsingException;
import com.lyncode.jtwig.manager.JtwigResource;

public interface Resolvable {
	void resolve (JtwigResource parent) throws IOException, JtwigParsingException;
	boolean replace (Block block);
}
