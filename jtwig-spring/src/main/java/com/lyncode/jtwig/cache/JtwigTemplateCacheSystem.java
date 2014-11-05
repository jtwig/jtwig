package com.lyncode.jtwig.cache;

import com.lyncode.jtwig.content.api.Renderable;

import java.util.concurrent.Callable;

public interface JtwigTemplateCacheSystem {
    Renderable get(String key, Callable<Renderable> instanceProvider);
}
