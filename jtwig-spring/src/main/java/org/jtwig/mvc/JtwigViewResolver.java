/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jtwig.mvc;

import org.jtwig.cache.JtwigTemplateCacheSystem;
import org.jtwig.cache.impl.PersistentTemplateCacheSystem;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.functions.SpringFunctions;
import org.jtwig.functions.parameters.convert.DemultiplexerConverter;
import org.jtwig.functions.parameters.convert.impl.ObjectToStringConverter;
import org.jtwig.functions.parameters.input.InputParameters;
import org.jtwig.functions.parameters.resolve.HttpRequestParameterResolver;
import org.jtwig.functions.parameters.resolve.api.InputParameterResolverFactory;
import org.jtwig.functions.parameters.resolve.api.ParameterResolver;
import org.jtwig.functions.parameters.resolve.impl.CompoundParameterResolver;
import org.jtwig.functions.parameters.resolve.impl.InputDelegateMethodParametersResolver;
import org.jtwig.functions.parameters.resolve.impl.ParameterAnnotationParameterResolver;
import org.jtwig.functions.resolver.api.FunctionResolver;
import org.jtwig.functions.resolver.impl.CompoundFunctionResolver;
import org.jtwig.functions.resolver.impl.DelegateFunctionResolver;
import org.jtwig.resource.loader.DefaultResourceResolver;
import org.jtwig.resource.loader.JtwigResourceResolver;
import org.jtwig.services.api.url.ResourceUrlResolver;
import org.jtwig.services.impl.url.IdentityUrlResolver;
import org.jtwig.services.impl.url.ThemedResourceUrlResolver;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import static org.jtwig.util.LocalThreadHolder.getServletRequest;

public class JtwigViewResolver extends AbstractTemplateViewResolver {

    private ThemeResolver themeResolver;
    private String encoding;
    private boolean cached = true;
    private boolean useThemeInViewPath = false;

    private JtwigResourceResolver loader;
    private JtwigConfiguration configuration = new JtwigConfiguration();
    private SpringFunctions springFunctions = null;
    private CompoundParameterResolver parameterResolver = new CompoundParameterResolver();
    private CompoundFunctionResolver functionResolver = new CompoundFunctionResolver()
            .withResolver(resolver(new DemultiplexerConverter()))
            .withResolver(resolver(new DemultiplexerConverter().withConverter(String.class, new ObjectToStringConverter())));
    private JtwigTemplateCacheSystem cache = new PersistentTemplateCacheSystem();


    public JtwigViewResolver() {
        setViewClass(requiredViewClass());
        setContentType("text/html; charset=UTF-8");

        parameterResolver
                .withResolver(new HttpRequestParameterResolver());
    }

    @Override
    protected Class<?> requiredViewClass() {
        return JtwigView.class;
    }

    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        AbstractUrlBasedView abstractUrlBasedView = super.buildView(viewName);
        abstractUrlBasedView.setUrl(urlResolver().resolve(getPrefix(), viewName, getSuffix()));
        return abstractUrlBasedView;
    }

    @Deprecated // Remove in 4.0.0
    public JtwigViewResolver setCached(boolean cached) {
        this.cached = cached;
        return this;
    }

    public JtwigViewResolver setThemeResolver(ThemeResolver themeResolver) {
        this.themeResolver = themeResolver;
        return this;
    }

    public JtwigViewResolver setUseThemeInViewPath(boolean useThemeInViewPath) {
        this.useThemeInViewPath = useThemeInViewPath;
        return this;
    }

    /**
     * Do not use. Use JtwigConfiguration.render().renderThreadingConfig().maxThreads()
     * @param value
     * @return
     */
    @Deprecated
    public JtwigViewResolver setConcurrentMaxThreads(int value) {
        configuration.render().renderThreadingConfig().maxThreads(value);
        return this;
    }

    /**
     * Do not use. Use JtwigConfiguration.render().renderThreadingConfig().minThreads()
     * @param value
     * @return
     */
    @Deprecated
    public JtwigViewResolver setConcurrentMinThreads(int value) {
        configuration.render().renderThreadingConfig().minThreads(value);
        return this;
    }

    public FunctionResolver functionResolver() {
        if (springFunctions == null) {
            springFunctions = new SpringFunctions();
            getApplicationContext().getAutowireCapableBeanFactory().autowireBean(springFunctions);
            configuration.render().functionRepository().include(springFunctions);
        }
        return functionResolver;
    }

    public JtwigViewResolver setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public JtwigConfiguration configuration() {
        return configuration;
    }

    public JtwigViewResolver setConfiguration(JtwigConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public JtwigViewResolver include(ParameterResolver resolver) {
        parameterResolver.withResolver(resolver);
        return this;
    }

    public JtwigViewResolver includeFunctions(Object functionBean) {
        configuration.render().functionRepository().include(functionBean);
        return this;
    }

    public JtwigViewResolver setResourceLoader(JtwigResourceResolver resourceLoader) {
        this.loader = resourceLoader;
        return this;
    }

    public JtwigViewResolver setCacheSystem(JtwigTemplateCacheSystem cache) {
        this.cache = cache;
        return this;
    }

    public boolean useThemeInViewPath() {
        return useThemeInViewPath;
    }

    // Methods only accessible to JtwigView, no need to give them to the end user
    JtwigResourceResolver resourceLoader() {
        if (loader == null) {
            setResourceLoader(new DefaultResourceResolver(getServletContext()));
        }
        return loader;
    }

    String getEncoding() {
        return encoding;
    }

    boolean isCached() {
        return cached;
    }

    JtwigTemplateCacheSystem cache() {
        return cache;
    }

    private InputParameterResolverFactory parameterResolverFactory(final DemultiplexerConverter converter) {
        return new InputParameterResolverFactory() {
            @Override
            public ParameterResolver create(InputParameters parameters) {
                return new CompoundParameterResolver()
                        .withResolver(new ParameterAnnotationParameterResolver(parameters, converter))
                        .withResolver(parameterResolver);
            }
        };
    }

    private DelegateFunctionResolver resolver(DemultiplexerConverter converter) {
        return new DelegateFunctionResolver(configuration.render().functionRepository(),
                                            new InputDelegateMethodParametersResolver(
                                                    parameterResolverFactory(converter)));
    }

    private ResourceUrlResolver urlResolver() {
        if (themeResolver == null || !useThemeInViewPath) {
            return IdentityUrlResolver.INSTANCE;
        } else {
            return new ThemedResourceUrlResolver(themeResolver.resolveThemeName(getServletRequest()));
        }
    }
}
