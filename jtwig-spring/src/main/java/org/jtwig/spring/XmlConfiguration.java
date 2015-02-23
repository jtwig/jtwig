package org.jtwig.spring;

import org.jtwig.addons.Addon;
import org.jtwig.cache.TemplateCache;
import org.jtwig.cache.impl.ExecutionCache;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.functions.config.JsonConfiguration;
import org.jtwig.functions.repository.api.FunctionRepository;
import org.jtwig.functions.repository.impl.MapFunctionRepository;
import org.jtwig.loader.Loader;
import org.jtwig.loader.impl.ChainLoader;
import org.jtwig.loader.impl.ClasspathLoader;
import org.jtwig.loader.impl.FileLoader;
import org.jtwig.parser.config.AddonParserList;
import org.jtwig.parser.config.Symbols;
import org.jtwig.parser.config.TagSymbols;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.asList;

public class XmlConfiguration implements JtwigConfiguration {
    private Charset charset = Charset.forName("UTF-8");
    private boolean strictMode = false;
    private boolean logNonStrictMode = false;
    private Symbols symbols = TagSymbols.DEFAULT;
    private AddonParserList addonParserList = new AddonParserList();
    private int minThreads = 20;
    private int maxThreads = 100;
    private long keepAliveTime = 60;
    private JsonConfiguration jsonConfiguration = new JsonConfiguration();
    private FunctionRepository functionRepository = new MapFunctionRepository(jsonConfiguration);
    private TemplateCache templateCache = new ExecutionCache();
    private Loader loader = new ChainLoader(asList(
        new ClasspathLoader(),
        new FileLoader(new String[]{"."})
    ));

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public void setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
    }

    public void setLogNonStrictMode(boolean logNonStrictMode) {
        this.logNonStrictMode = logNonStrictMode;
    }

    public void setSymbols(Symbols symbols) {
        this.symbols = symbols;
    }

    public void setAddonParserList(AddonParserList addonParserList) {
        this.addonParserList = addonParserList;
    }

    public void setMinThreads(int minThreads) {
        this.minThreads = minThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public void setJsonConfiguration(JsonConfiguration jsonConfiguration) {
        this.jsonConfiguration = jsonConfiguration;
    }

    public void setFunctionRepository(FunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
    }

    public void setTemplateCache(TemplateCache templateCache) {
        this.templateCache = templateCache;
    }

    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    public void setExtraFunctionContainers(Object[] instances) {
        for (Object instance : instances) {
            getFunctionRepository().include(instance);
        }
    }

    public void setSymbols(String type) {
        setSymbols(TagSymbols.valueOf(type));
    }

    public void setExtraSyntaticAddons(Class<? extends Addon>[] addons) {
        for (Class<? extends Addon> addon : addons) {
            getAddonParserList().withAddon(addon);
        }
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
