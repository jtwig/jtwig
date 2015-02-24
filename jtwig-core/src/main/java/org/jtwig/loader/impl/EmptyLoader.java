package org.jtwig.loader.impl;

import org.jtwig.exception.ResourceException;
import org.jtwig.loader.Loader;

import java.io.InputStream;

public class EmptyLoader extends Loader {

    @Override
    public boolean exists(String name) throws ResourceException {
        return false;
    }

    @Override
    public Resource get(String name) throws ResourceException {
        return new NoResource(name);
    }

    public static class NoResource extends Resource {
        private final String name;

        public NoResource(String name) {
            this.name = name;
        }

        @Override
        public String getCacheKey() {
            return name;
        }

        @Override
        public String canonicalPath() {
            return name;
        }

        @Override
        public String relativePath() {
            return name;
        }

        @Override
        public InputStream source() throws ResourceException {
            throw new ResourceException(String.format("Template '%s' not found", name));
        }

        @Override
        public String resolve(String relative) throws ResourceException {
            throw new ResourceException(String.format("Unknown template '%s' without '%s' relative path", name, relative));
        }
    }
}
