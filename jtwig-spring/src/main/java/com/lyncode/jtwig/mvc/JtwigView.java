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

import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.beans.BeanResolver;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.types.Undefined;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.ui.context.Theme;
import org.springframework.web.servlet.view.AbstractTemplateView;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.servlet.support.RequestContextUtils.getTheme;

public class JtwigView extends AbstractTemplateView {

    private static Logger log = LoggerFactory.getLogger(JtwigView.class);

    private Map<String, Renderable> compiledTemplates = new HashMap<>();

    protected String getEncoding() {
        return getViewResolver().getEncoding();
    }

    protected JtwigConfiguration getConfiguration() {
        return getViewResolver().configuration();
    }

    private JtwigViewResolver getViewResolver() {
        return this.getApplicationContext().getBean(JtwigViewResolver.class);
    }

    protected void initApplicationContext() throws BeansException {
        super.initApplicationContext();
        GenericServlet servlet = new GenericServletAdapter();
        try {
            servlet.init(new DelegatingServletConfig());
        } catch (ServletException ex) {
            throw new BeanInitializationException("Initialization of GenericServlet adapter failed", ex);
        }
    }

    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request,
                                             HttpServletResponse response) throws Exception {

        // Adding model information
        JtwigModelMap modelMap = new JtwigModelMap()
                .add(model)
                .add("beans", new BeanResolver(getApplicationContext()))
                .add("theme", getThemeName(request))
                .add("request", request);

        Object token = request.getAttribute("org.springframework.security.web.csrf.CsrfToken");
        if(token != null){
            modelMap.add("csrf", token);
        }else{
            modelMap.add("csrf", Undefined.UNDEFINED);
        }

        if (log.isDebugEnabled()) {
            log.debug("Rendering Jtwig templates [" + getUrl() + "] in JtwigView '" + getBeanName() + "'");
            log.debug("Model: " + modelMap);
        }

        if (this.getEncoding() != null) {
            response.setCharacterEncoding(this.getEncoding());
        }

        getContent().render(RenderContext.create(getConfiguration().render(), modelMap, getViewResolver().functionResolver(), response.getOutputStream()));

        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    private String getThemeName(HttpServletRequest request) {
        Theme theme = getTheme(request);
        if (theme == null)
            return null;
        return theme.getName();
    }

    public Renderable getContent() throws CompileException, ParseException {
        if (getViewResolver().isCached()) {
            if (!compiledTemplates.containsKey(getUrl())) {
                compiledTemplates.put(getUrl(), getCompiledJtwigTemplate());
            }
            return compiledTemplates.get(getUrl());
        }
        return getCompiledJtwigTemplate();
    }

    private Renderable getCompiledJtwigTemplate() throws ParseException, CompileException {
        return new JtwigTemplate(getResource(), getConfiguration()).compile();
    }

    private JtwigResource getResource() {
        return getViewResolver()
                .resourceLoader()
                .resolve(getUrl());
    }

    @SuppressWarnings("serial")
    private static class GenericServletAdapter extends GenericServlet {

        public void service(ServletRequest servletRequest, ServletResponse servletResponse) {
            // no-op
        }
    }

    private class DelegatingServletConfig implements ServletConfig {

        public String getServletName() {
            return JtwigView.this.getBeanName();
        }

        public ServletContext getServletContext() {
            return JtwigView.this.getServletContext();
        }

        public String getInitParameter(String paramName) {
            return null;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        public Enumeration getInitParameterNames() {
            return Collections.enumeration(Collections.EMPTY_SET);
        }
    }
}
