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

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.beans.BeanResolver;
import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.content.api.Renderable;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.parser.JtwigParser;
import com.lyncode.jtwig.render.RenderContext;
import com.lyncode.jtwig.resource.ClasspathJtwigResource;
import com.lyncode.jtwig.resource.FileJtwigResource;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.resource.WebJtwigResource;
import com.lyncode.jtwig.types.Undefined;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.servlet.view.AbstractTemplateView;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class JtwigView extends AbstractTemplateView {

    private static Logger log = LogManager.getLogger(JtwigView.class);

    private Map<String, Renderable> compiledTemplates = new HashMap<>();

    protected String getEncoding() {
        return getViewResolver().getEncoding();
    }

    protected String getTheme() {
        return getViewResolver().getTheme();
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
                .add("theme", getTheme())
                .add("request", request);

        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if(token != null){
            modelMap.add("csrf", token);
        }else{
            modelMap.add("csrf", Undefined.UNDEFINED);
        }

        if (log.isDebugEnabled()) {
            log.debug("Rendering Jtwig templates [" + getUrl() + "] in JtwigView '" + getBeanName() + "'");
            log.debug("Model: " + modelMap);
        }

        response.setContentType(this.getContentType());
        if (this.getEncoding() != null) {
            response.setCharacterEncoding(this.getEncoding());
        }

        JtwigContext jtwigContext = new JtwigContext(modelMap, getViewResolver().getFunctionResolver());
        getContent(request).render(RenderContext.create(getConfiguration().render(), jtwigContext, response.getOutputStream()));

        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    public Renderable getContent(HttpServletRequest request) throws CompileException, ParseException {
        if (getViewResolver().isCached()) {
            if (!compiledTemplates.containsKey(getUrl())) {
                compiledTemplates.put(getUrl(), getCompiledJtwigTemplate(request));
            }
            return compiledTemplates.get(getUrl());
        }
        return getCompiledJtwigTemplate(request);
    }

    private Renderable getCompiledJtwigTemplate(HttpServletRequest request) throws ParseException, CompileException {
        return new JtwigTemplate(getResource(request))
                .compile(jtwigParser());
    }

    private JtwigResource getResource(HttpServletRequest request) {
        String prefix = getViewResolver().getPrefix();
        if (prefix.startsWith("classpath:"))
            return new ClasspathJtwigResource(getUrl());
        else if (prefix.startsWith("file://"))
            return new FileJtwigResource(getUrl());
        else
            return new WebJtwigResource(getServletContext(), getUrl());
    }

    private JtwigParser jtwigParser() {
        return new JtwigParser(getConfiguration().parse());
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
