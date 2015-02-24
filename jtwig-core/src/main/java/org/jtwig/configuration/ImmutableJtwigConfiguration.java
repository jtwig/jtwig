package org.jtwig.configuration;

import org.jtwig.cache.TemplateCache;
import org.jtwig.functions.config.JsonConfiguration;
import org.jtwig.functions.repository.api.FunctionRepository;
import org.jtwig.loader.Loader;
import org.jtwig.parser.config.AddonParserList;
import org.jtwig.parser.config.Symbols;

import java.nio.charset.Charset;

public class ImmutableJtwigConfiguration implements JtwigConfiguration {
    private final Charset charset;
    private final boolean strictMode;
    private final boolean logNonStrictMode;
    private final Symbols symbols;
    private final AddonParserList addonParserList;
    private final int minThreads;
    private final int maxThreads;
    private final long keepAliveTime;
    private final JsonConfiguration jsonConfiguration;
    private final FunctionRepository functionRepository;
    private final TemplateCache templateCache;
    private final Loader loader;

    public ImmutableJtwigConfiguration(Charset charset, boolean strictMode, boolean logNonStrictMode, Symbols symbols, AddonParserList addonParserList, int minThreads, int maxThreads, long keepAliveTime,
                                JsonConfiguration jsonConfiguration, FunctionRepository functionRepository, TemplateCache templateCache, Loader loader) {
        this.charset = charset;
        this.strictMode = strictMode;
        this.logNonStrictMode = logNonStrictMode;
        this.symbols = symbols;
        this.addonParserList = addonParserList;
        this.minThreads = minThreads;
        this.maxThreads = maxThreads;
        this.keepAliveTime = keepAliveTime;
        this.jsonConfiguration = jsonConfiguration;
        this.functionRepository = functionRepository;
        this.templateCache = templateCache;
        this.loader = loader;
    }

    @Override
    public Charset getCharset() {
        return charset;
    }

    @Override
    public boolean isStrictMode() {
        return strictMode;
    }

    @Override
    public boolean isLogNonStrictMode() {
        return logNonStrictMode;
    }

    @Override
    public Symbols getSymbols() {
        return symbols;
    }

    @Override
    public AddonParserList getAddonParserList() {
        return addonParserList;
    }

    @Override
    public int getMinThreads() {
        return minThreads;
    }

    @Override
    public int getMaxThreads() {
        return maxThreads;
    }

    @Override
    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    @Override
    public JsonConfiguration getJsonConfiguration() {
        return jsonConfiguration;
    }

    @Override
    public FunctionRepository getFunctionRepository() {
        return functionRepository;
    }

    @Override
    public TemplateCache getTemplateCache() {
        return templateCache;
    }

    @Override
    public Loader getLoader() {
        return loader;
    }
}
