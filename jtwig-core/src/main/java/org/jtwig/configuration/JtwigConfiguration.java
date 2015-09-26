package org.jtwig.configuration;

import org.jtwig.cache.TemplateCache;
import org.jtwig.functions.config.JsonConfiguration;
import org.jtwig.functions.repository.api.FunctionRepository;
import org.jtwig.loader.Loader;
import org.jtwig.parser.config.Symbols;

import java.nio.charset.Charset;
import org.jtwig.extension.ExtensionHolder;

public interface JtwigConfiguration {

    Charset getCharset();

    boolean isStrictMode();

    boolean isLogNonStrictMode();

    Symbols getSymbols();

    ExtensionHolder getExtensions();

    int getMinThreads();

    int getMaxThreads();

    long getKeepAliveTime();

    JsonConfiguration getJsonConfiguration();

    FunctionRepository getFunctionRepository();

    TemplateCache getTemplateCache();

    Loader getLoader();
}
