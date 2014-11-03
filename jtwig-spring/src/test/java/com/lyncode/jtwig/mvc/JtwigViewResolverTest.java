package com.lyncode.jtwig.mvc;

import com.lyncode.jtwig.configuration.JtwigConfiguration;
import com.lyncode.jtwig.resource.JtwigResource;
import com.lyncode.jtwig.resource.StringJtwigResource;
import com.lyncode.jtwig.resource.loader.JtwigResourceResolver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JtwigViewResolverTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ModelMap model;
    private ServletContext servletContext;
    private GenericWebApplicationContext applicationContext;
    private JtwigViewResolver underTest;


    @Before
    public void setUp() throws Exception {
        underTest = new JtwigViewResolver();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        model = new ModelMap();
    }

    @Test
    public void renders() throws Exception {
        servletContext = new MockServletContext();
        applicationContext = new GenericWebApplicationContext(servletContext);
        applicationContext.getBeanFactory().registerSingleton("viewResolver", underTest);
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);

        underTest.setApplicationContext(applicationContext);
        underTest.setServletContext(servletContext);
        underTest.setResourceLoader(new JtwigResourceResolver() {
            @Override
            public JtwigResource resolve(String viewUrl) {
                return new StringJtwigResource("test");
            }
        });

        AbstractUrlBasedView one = underTest.buildView("one");
        assertThat(one, instanceOf(JtwigView.class));

        one.setApplicationContext(applicationContext);
        one.setServletContext(servletContext);
        one.afterPropertiesSet();

        one.render(model, request, response);
    }

    @Test
    public void includeFunctionDelegatesToRenderConfiguration() throws Exception {
        servletContext = new MockServletContext();
        applicationContext = new GenericWebApplicationContext(servletContext);
        applicationContext.getBeanFactory().registerSingleton("viewResolver", underTest);
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);

        JtwigConfiguration configuration = mock(JtwigConfiguration.class, Mockito.RETURNS_DEEP_STUBS);
        underTest.setConfiguration(configuration);

        underTest.includeFunctions(this);

        verify(configuration.render().functionRepository()).include(this);
    }
}