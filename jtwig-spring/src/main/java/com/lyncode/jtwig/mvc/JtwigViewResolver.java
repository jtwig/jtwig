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

import com.lyncode.jtwig.functions.SpringFunctions;
import com.lyncode.jtwig.functions.parameters.resolve.HttpRequestParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.api.AnnotatedMethodParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.api.TypeMethodParameterResolver;
import com.lyncode.jtwig.functions.repository.FunctionRepositoryBuilder;
import com.lyncode.jtwig.functions.repository.FunctionResolver;
import com.lyncode.jtwig.parser.config.ParserConfiguration;
import com.lyncode.jtwig.services.api.theme.ThemePrefixResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

import java.io.File;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class JtwigViewResolver extends AbstractTemplateViewResolver {
    private static ThemePrefixResolver defaultPrefixResolver() {
        return new ThemePrefixResolver() {
            @Override
            public String getPrefix(String prefix, String theme) {
                return new File(prefix, theme).getPath();
            }
        };
    }

    @Autowired(required = false)
    private ThemePrefixResolver prefixResolver ;

    private String encoding;
    private String theme;
    private boolean cached = true;

    private ParserConfiguration parserConfiguration = new ParserConfiguration();
    private FunctionRepositoryBuilder functionRepository = new FunctionRepositoryBuilder();
    private FunctionResolver functionResolver = null;

    public JtwigViewResolver() {
        setViewClass(requiredViewClass());
        setContentType("text/html; charset=UTF-8");

        functionRepository.add(new HttpRequestParameterResolver());
    }

    @Override
    protected Class<?> requiredViewClass() {
        return JtwigView.class;
    }

    @Override
    protected String getPrefix() {
        if (hasTheme())
            return getPrefixWithTheme();
        else
            return super.getPrefix();
    }

    private String getPrefixWithTheme() {
        if (prefixResolver == null) prefixResolver = defaultPrefixResolver();
        String prefix = prefixResolver.getPrefix(super.getPrefix(), getTheme());
        if (super.getPrefix().endsWith(File.separator))
            prefix += File.separator;
        return prefix;
    }

    public boolean isCached() {
        return cached;
    }

    public String getTheme() {
        return this.theme;
    }

    public boolean hasTheme() {
        return isNotBlank(theme);
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    public void setParserConfiguration (ParserConfiguration parserConfiguration) { this.parserConfiguration = parserConfiguration; }

    ParserConfiguration getParserConfiguration() {
        return parserConfiguration;
    }
    FunctionResolver getFunctionResolver() {
        if (functionResolver == null) {
            SpringFunctions springFunctions = new SpringFunctions();
            getApplicationContext().getAutowireCapableBeanFactory().autowireBean(springFunctions);
            functionRepository.store(springFunctions);
        }
        return functionRepository.build();
    }

    public JtwigViewResolver include (TypeMethodParameterResolver resolver) {
        functionRepository.add(resolver);
        return this;
    }

    public JtwigViewResolver include (AnnotatedMethodParameterResolver resolver) {
        functionRepository.add(resolver);
        return this;
    }

    public JtwigViewResolver includeFunctions (Object functionBean) {
        functionRepository.store(functionBean);
        return this;
    }
}
