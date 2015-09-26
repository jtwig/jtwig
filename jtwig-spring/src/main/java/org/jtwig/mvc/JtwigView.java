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

import org.jtwig.JtwigModelMap;
import org.jtwig.beans.BeanResolver;
import org.jtwig.exception.CompileException;
import org.jtwig.exception.ParseException;
import org.jtwig.render.RenderContext;
import org.jtwig.types.Undefined;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.ui.context.Theme;
import org.springframework.web.servlet.view.AbstractTemplateView;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import org.jtwig.Environment;
import org.jtwig.content.model.Template;
import org.jtwig.exception.ResourceException;

import static org.springframework.web.servlet.support.RequestContextUtils.getTheme;

public class JtwigView extends AbstractTemplateView {
    private static final String SPRING_CSRF = "org.springframework.security.web.csrf.CsrfToken";
    
    protected String getEncoding() {
        return getViewResolver().getEncoding();
    }

    protected Environment getEnvironment() {
        return getViewResolver().getEnvironment();
    }

    private JtwigViewResolver getViewResolver() {
        return this.getApplicationContext().getBean(JtwigViewResolver.class);
    }

    @Override
    protected void initApplicationContext() throws BeansException {
        super.initApplicationContext();
        GenericServlet servlet = getGenericServlet();
        try {
            servlet.init(getServletConfig());
        } catch (ServletException ex) {
            throw new BeanInitializationException("Initialization of GenericServlet adapter failed", ex);
        }
    }
    
    protected GenericServlet getGenericServlet() {
        return new GenericServletAdapter();
    }
    
    protected ServletConfig getServletConfig() {
        return new DelegatingServletConfig();
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

        Object token = request.getAttribute(SPRING_CSRF);
        if (token != null) {
            modelMap.add("csrf", token);
        } else {
            modelMap.add("csrf", Undefined.UNDEFINED);
        }

        if (this.getEncoding() != null) {
            response.setCharacterEncoding(this.getEncoding());
        }

        getCompiledJtwigTemplate().render(RenderContext.create(getEnvironment(), modelMap, getViewResolver().functionResolver(), response.getOutputStream()));

        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    private String getThemeName(HttpServletRequest request) {
        Theme theme = getTheme(request);
        if (theme == null)
            return null;
        return theme.getName();
    }

//    public Renderable getContent() throws CompileException, ParseException {
//        return getEnvironment().compile(SPRING_CSRF)
//        return getViewResolver().cache().get(getUrl(), new Callable<Renderable>() {
//            @Override
//            public Renderable call() throws Exception {
//                return getCompiledJtwigTemplate();
//            }
//        });
//    }

    private Template.Compiled getCompiledJtwigTemplate() throws ResourceException, ParseException, CompileException {
        return getEnvironment().compile(getUrl());
    }

//    private Loader.Resource getResource() throws ResourceException {
//        return getEnvironment()
//                .load(getUrl());
//    }

    @SuppressWarnings("serial")
    private static class GenericServletAdapter extends GenericServlet {

        @Override
        public void service(ServletRequest servletRequest, ServletResponse servletResponse) {
            // no-op
        }
    }

    private class DelegatingServletConfig implements ServletConfig {

        @Override
        public String getServletName() {
            return JtwigView.this.getBeanName();
        }

        @Override
        public ServletContext getServletContext() {
            return JtwigView.this.getServletContext();
        }

        @Override
        public String getInitParameter(String paramName) {
            return null;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public Enumeration getInitParameterNames() {
            return Collections.enumeration(Collections.EMPTY_SET);
        }
    }
}
