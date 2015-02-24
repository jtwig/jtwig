package org.jtwig.unit.mvc;

import java.io.ByteArrayInputStream;
import org.jtwig.cache.JtwigTemplateCacheSystem;
import org.jtwig.configuration.JtwigConfigurationBuilder;
import org.jtwig.content.api.Renderable;
import org.jtwig.mvc.JtwigView;
import org.jtwig.mvc.JtwigViewResolver;
import org.junit.Before;
import org.junit.Test;

import static org.jtwig.configuration.JtwigConfigurationBuilder.newConfiguration;
import static org.junit.Assert.*;
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
import org.jtwig.Environment;
import org.jtwig.cache.TemplateCache;
import org.jtwig.loader.Loader;
import org.jtwig.loader.impl.StringLoader;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class JtwigViewResolverTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ModelMap model;
    private ServletContext servletContext;
    private GenericWebApplicationContext applicationContext;
    private JtwigViewResolver underTest;
    private TemplateCache cache;
    private Environment env;


    @Before
    public void setUp() throws Exception {
        env = spy(new Environment());
        cache = mock(TemplateCache.class);
        when(env.getConfiguration()).thenReturn(newConfiguration().withTemplateCache(cache).build());
        
        underTest = new JtwigViewResolver();
        underTest.setEnvironment(env);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        model = new ModelMap();
    }


    @Test
    public void includeFunctionDelegatesToRenderConfiguration() throws Exception {
        servletContext = new MockServletContext();
        applicationContext = new GenericWebApplicationContext(servletContext);
        applicationContext.getBeanFactory().registerSingleton("viewResolver", underTest);
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);

        Environment env = mock(Environment.class, Mockito.RETURNS_DEEP_STUBS);
        underTest.setEnvironment(env);

        underTest.includeFunctions(this);

        verify(env.getConfiguration().getFunctionRepository()).include(this);
    }
}
