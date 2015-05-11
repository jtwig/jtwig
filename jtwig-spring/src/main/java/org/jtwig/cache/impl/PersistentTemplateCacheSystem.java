package org.jtwig.cache.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.jtwig.cache.JtwigTemplateCacheSystem;
import org.jtwig.content.api.Renderable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class PersistentTemplateCacheSystem implements JtwigTemplateCacheSystem {
    private final Cache<String, Renderable> cache = CacheBuilder.<String, Renderable>newBuilder().build();

    @Override
    public Renderable get(String key, Callable<Renderable> instanceProvider) {
        try {
            return cache.get(key, instanceProvider);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }
}
