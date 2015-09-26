package org.jtwig.configuration;

import org.jtwig.addons.Addon;
import org.jtwig.cache.TemplateCache;
import org.jtwig.cache.impl.ExecutionCache;
import org.jtwig.functions.config.JsonConfiguration;
import org.jtwig.functions.repository.api.FunctionRepository;
import org.jtwig.functions.repository.impl.MapFunctionRepository;
import org.jtwig.loader.Loader;
import org.jtwig.loader.impl.ChainLoader;
import org.jtwig.loader.impl.ClasspathLoader;
import org.jtwig.loader.impl.FileLoader;
import org.jtwig.parser.config.Symbols;
import org.jtwig.parser.config.TagSymbols;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.asList;
import org.jtwig.extension.ExtensionHolder;
import org.jtwig.extension.core.CoreJtwigExtension;

public class JtwigConfigurationBuilder {
    public static JtwigConfigurationBuilder newConfiguration() {
        return new JtwigConfigurationBuilder();
    }
    public static JtwigConfiguration defaultConfiguration() {
        JtwigConfiguration config = new JtwigConfigurationBuilder().build();
        CoreJtwigExtension core = new CoreJtwigExtension(config);
        config.getExtensions().addExtension(core);
        return config;
    }

    private Charset charset = Charset.forName("UTF-8");
    private boolean strictMode = false;
    private boolean logNonStrictMode = false;
    private Symbols symbols = TagSymbols.DEFAULT;
    private ExtensionHolder extensions = new ExtensionHolder();
    private int minThreads = 20;
    private int maxThreads = 100;
    private long keepAliveTime = 60;
    private JsonConfiguration jsonConfiguration = new JsonConfiguration();
    private FunctionRepository functionRepository;
    private Collection<Object> functionBeans = new ArrayList<>();
    private TemplateCache templateCache = new ExecutionCache();
    private Loader loader = new ChainLoader(asList(
        new ClasspathLoader(),
        new FileLoader(new String[]{"."})
    ));

    private JtwigConfigurationBuilder() {
    }

    public JtwigConfigurationBuilder withCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public JtwigConfigurationBuilder withStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
        return this;
    }

    public JtwigConfigurationBuilder withLogNonStrictMode(boolean logNonStrictMode) {
        this.logNonStrictMode = logNonStrictMode;
        return this;
    }

    public JtwigConfigurationBuilder withSymbols(Symbols symbols) {
        this.symbols = symbols;
        return this;
    }

    public JtwigConfigurationBuilder withExtensions(ExtensionHolder extensions) {
        this.extensions = extensions;
        return this;
    }
    public ExtensionHolder getExtensions() {
        return extensions;
    }

    public JtwigConfigurationBuilder withMinThreads(int minThreads) {
        this.minThreads = minThreads;
        return this;
    }

    public JtwigConfigurationBuilder withMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
        return this;
    }

    public JtwigConfigurationBuilder withKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
        return this;
    }

    public JtwigConfigurationBuilder withJsonConfiguration(JsonConfiguration jsonConfiguration) {
        this.jsonConfiguration = jsonConfiguration;
        return this;
    }

    public JtwigConfigurationBuilder withFunctionRepository(FunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
        return this;
    }

    public JtwigConfigurationBuilder withTemplateCache(TemplateCache templateCache) {
        this.templateCache = templateCache;
        return this;
    }

    public JtwigConfigurationBuilder withLoader(Loader loader) {
        this.loader = loader;
        return this;
    }

    public JtwigConfigurationBuilder include(Object instance) {
        this.functionBeans.add(instance);
        return this;
    }

    public JtwigConfiguration build() {
        return new ImmutableJtwigConfiguration(charset, strictMode, logNonStrictMode, symbols, extensions, minThreads, maxThreads, keepAliveTime, jsonConfiguration, getFunctionRepository(), templateCache,
                                      loader);
    }

    private FunctionRepository getFunctionRepository() {
        if (functionRepository == null)
            functionRepository = new MapFunctionRepository(jsonConfiguration);

        for (Object functionBean : functionBeans) {
            functionRepository.include(functionBean);
        }

        return functionRepository;
    }
}
