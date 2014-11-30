package org.jtwig.unit.mvc;

import org.jtwig.cache.JtwigTemplateCacheSystem;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.content.api.Renderable;
import org.jtwig.mvc.JtwigView;
import org.jtwig.mvc.JtwigViewResolver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
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
import java.util.Locale;
import java.util.concurrent.Callable;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
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
        JtwigTemplateCacheSystem cacheSystem = mock(JtwigTemplateCacheSystem.class);
        Mockito.when(cacheSystem.get(anyString(), Matchers.any(Callable.class))).thenReturn(mock(Renderable.class));


        servletContext = new MockServletContext();
        applicationContext = new GenericWebApplicationContext(servletContext);
        applicationContext.getBeanFactory().registerSingleton("viewResolver", underTest);
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);

        underTest.setApplicationContext(applicationContext);
        underTest.setServletContext(servletContext);
        underTest.setCacheSystem(cacheSystem);

        AbstractUrlBasedView one = (AbstractUrlBasedView) underTest.resolveViewName("one", Locale.ENGLISH);
        assertThat(one, instanceOf(JtwigView.class));

        one.setApplicationContext(applicationContext);
        one.setServletContext(servletContext);
        one.afterPropertiesSet();

        one.render(model, request, response);
        verify(cacheSystem).get(eq("one"), Matchers.any(Callable.class));
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
