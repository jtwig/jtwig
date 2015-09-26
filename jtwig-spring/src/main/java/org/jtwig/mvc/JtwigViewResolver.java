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

import org.jtwig.Environment;
import org.jtwig.cache.JtwigTemplateCacheSystem;
import org.jtwig.cache.impl.PersistentTemplateCacheSystem;
import org.jtwig.extension.spring.SpringExtension;
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
import org.jtwig.services.api.url.ResourceUrlResolver;
import org.jtwig.services.impl.url.IdentityUrlResolver;
import org.jtwig.services.impl.url.ThemedResourceUrlResolver;
import static org.jtwig.util.LocalThreadHolder.getServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class JtwigViewResolver extends AbstractTemplateViewResolver {

    private ThemeResolver themeResolver;
    private String encoding;
    private boolean useThemeInViewPath = false;

    private Environment env;
    private SpringExtension springExtension;
    private CompoundParameterResolver parameterResolver = new CompoundParameterResolver();
    private CompoundFunctionResolver functionResolver;
    private JtwigTemplateCacheSystem cache = new PersistentTemplateCacheSystem();


    public JtwigViewResolver() {
        this(new Environment());
    }
    public JtwigViewResolver(Environment env) {
        this.env = env;
        setViewClass(requiredViewClass());
        setContentType("text/html; charset=UTF-8");

        parameterResolver
                .withResolver(new HttpRequestParameterResolver());
        functionResolver = new CompoundFunctionResolver()
            .withResolver(resolver(new DemultiplexerConverter()))
            .withResolver(resolver(new DemultiplexerConverter().withConverter(String.class, new ObjectToStringConverter())));
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

    public JtwigViewResolver setThemeResolver(ThemeResolver themeResolver) {
        this.themeResolver = themeResolver;
        return this;
    }

    public JtwigViewResolver setUseThemeInViewPath(boolean useThemeInViewPath) {
        this.useThemeInViewPath = useThemeInViewPath;
        return this;
    }

    public FunctionResolver functionResolver() {
        if (springExtension == null) {
            springExtension = new SpringExtension();
            getApplicationContext().getAutowireCapableBeanFactory().autowireBean(springExtension);
            env.getConfiguration().getExtensions().addExtension(springExtension);
        }
        return functionResolver;
    }

    public JtwigViewResolver setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public Environment getEnvironment() {
        return env;
    }

    @Autowired
    public JtwigViewResolver setEnvironment(Environment env) {
        this.env = env;
        return this;
    }

    public JtwigViewResolver include(ParameterResolver resolver) {
        parameterResolver.withResolver(resolver);
        return this;
    }

    public JtwigViewResolver includeFunctions(Object functionBean) {
        env.getConfiguration().getFunctionRepository().include(functionBean);
        return this;
    }

    public JtwigViewResolver setCacheSystem(JtwigTemplateCacheSystem cache) {
        this.cache = cache;
        return this;
    }

    public boolean useThemeInViewPath() {
        return useThemeInViewPath;
    }

    String getEncoding() {
        return encoding;
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
        return new DelegateFunctionResolver(env.getConfiguration().getFunctionRepository(),
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
