package org.jtwig.cache.impl;

import org.jtwig.cache.JtwigTemplateCacheSystem;
import org.jtwig.content.api.Renderable;

import java.util.concurrent.Callable;

public class NoCacheSystem implements JtwigTemplateCacheSystem {
    @Override
    public Renderable get(String key, Callable<Renderable> instanceProvider) {
        try {
            return instanceProvider.call();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
