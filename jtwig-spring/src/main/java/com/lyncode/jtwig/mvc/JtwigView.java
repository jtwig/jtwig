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
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.resource.WebJtwigResource;
import com.lyncode.jtwig.tree.api.Content;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
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

    private Map<String, Content> compiledTemplates = new HashMap<>();

    protected String getEncoding() {
        return getViewResolver().getEncoding();
    }
    protected String getTheme() {
        return getViewResolver().getTheme();
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
    protected void renderMergedTemplateModel(Map<String, Object> model,
                                             HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // Adding model information
        JtwigModelMap modelMap = new JtwigModelMap()
                .add(model)
                .add("theme", getTheme())
                .add("request", request)
            ;

        if (log.isDebugEnabled()) {
            log.debug("Rendering Jtwig template [" + getUrl() + "] in JtwigView '" + getBeanName() + "'");
            log.debug("Model: "+modelMap);
        }


        response.setContentType(this.getContentType());
        if (this.getEncoding() != null)
            response.setCharacterEncoding(this.getEncoding());

        getContent(request).render(response.getOutputStream(), new JtwigContext(modelMap, getViewResolver().getFunctionRepository()));
    }

    public Content getContent(HttpServletRequest request) throws CompileException, ParseException {
        if (getViewResolver().isCached()) {
            if (!compiledTemplates.containsKey(getUrl())) {
                compiledTemplates.put(getUrl(), getCompiledJtwigTemplate(request));
            }
            return compiledTemplates.get(getUrl());
        }
        return getCompiledJtwigTemplate(request);
    }

    private Content getCompiledJtwigTemplate(HttpServletRequest request) throws ParseException, CompileException {
        return new JtwigTemplate(new WebJtwigResource(request.getSession().getServletContext(), getUrl())).compile();
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

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public Enumeration getInitParameterNames() {
            return Collections.enumeration(Collections.EMPTY_SET);
        }
    }
}
