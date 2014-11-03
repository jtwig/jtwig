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

package com.lyncode.jtwig.mvc;

import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.functions.SpringFunctions;
import com.lyncode.jtwig.functions.parameters.convert.DemultiplexerConverter;
import com.lyncode.jtwig.functions.parameters.convert.impl.ObjectToStringConverter;
import com.lyncode.jtwig.functions.parameters.input.InputParameters;
import com.lyncode.jtwig.functions.parameters.resolve.HttpRequestParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.api.InputParameterResolverFactory;
import com.lyncode.jtwig.functions.parameters.resolve.api.ParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.impl.CompoundParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.impl.InputDelegateMethodParametersResolver;
import com.lyncode.jtwig.functions.parameters.resolve.impl.ParameterAnnotationParameterResolver;
import com.lyncode.jtwig.functions.resolver.api.FunctionResolver;
import com.lyncode.jtwig.functions.resolver.impl.CompoundFunctionResolver;
import com.lyncode.jtwig.functions.resolver.impl.DelegateFunctionResolver;
import com.lyncode.jtwig.resource.loader.DefaultResourceResolver;
import com.lyncode.jtwig.resource.loader.JtwigResourceResolver;
import com.lyncode.jtwig.services.api.url.ResourceUrlResolver;
import com.lyncode.jtwig.services.impl.url.IdentityUrlResolver;
import com.lyncode.jtwig.services.impl.url.ThemedResourceUrlResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import static com.lyncode.jtwig.render.stream.RenderStream.withMaxThreads;
import static com.lyncode.jtwig.render.stream.RenderStream.withMinThreads;
import static com.lyncode.jtwig.util.LocalThreadHolder.getServletRequest;

@Service
public class JtwigViewResolver extends AbstractTemplateViewResolver {
    @Autowired(required = false)
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

    public JtwigViewResolver setConcurrentMaxThreads(int value) {
        withMaxThreads(value);
        return this;
    }

    public JtwigViewResolver setConcurrentMinThreads(int value) {
        withMinThreads(value);
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

    public JtwigViewResolver include (ParameterResolver resolver) {
        parameterResolver.withResolver(resolver);
        return this;
    }

    public JtwigViewResolver includeFunctions (Object functionBean) {
        configuration.render().functionRepository().include(functionBean);
        return this;
    }

    public JtwigViewResolver setResourceLoader(DefaultResourceResolver resourceLoader) {
        this.loader = resourceLoader;
        return this;
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
        return new DelegateFunctionResolver(configuration.render().functionRepository(), new InputDelegateMethodParametersResolver(parameterResolverFactory(converter)));
    }

    private ResourceUrlResolver urlResolver() {
        if (themeResolver == null || !useThemeInViewPath)
            return IdentityUrlResolver.INSTANCE;
        else
            return new ThemedResourceUrlResolver(themeResolver.resolveThemeName(getServletRequest()));
    }
}
