package org.jtwig.unit.mvc;

import java.io.ByteArrayInputStream;
import org.jtwig.cache.JtwigTemplateCacheSystem;
import org.jtwig.content.api.Renderable;
import org.jtwig.mvc.JtwigView;
import org.jtwig.mvc.JtwigViewResolver;
import org.junit.Before;
import org.junit.Test;
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
        env.setCache(cache = mock(TemplateCache.class));
        
        underTest = new JtwigViewResolver();
        underTest.setEnvironment(env);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        model = new ModelMap();
    }

    @Test
    public void renders() throws Exception {
//        JtwigTemplateCacheSystem cacheSystem = mock(JtwigTemplateCacheSystem.class);
//        Mockito.when(cacheSystem.get(anyString(), Matchers.any(Callable.class))).thenReturn(mock(Renderable.class));

        Loader.Resource oneResource = mock(Loader.Resource.class);
        when(oneResource.source()).thenReturn(new ByteArrayInputStream("".getBytes()));
        when(oneResource.getCacheKey()).thenReturn("one");
        when(oneResource.relativePath()).thenReturn("one");
        when(oneResource.canonicalPath()).thenReturn("one");
        
        Loader loader = mock(Loader.class);
        when(loader.exists("one")).thenReturn(true);
        when(loader.get("one")).thenReturn(oneResource);
        env.setLoader(loader);

        servletContext = new MockServletContext();
        applicationContext = new GenericWebApplicationContext(servletContext);
        applicationContext.getBeanFactory().registerSingleton("viewResolver", underTest);
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);

        underTest.setApplicationContext(applicationContext);
        underTest.setServletContext(servletContext);
//        underTest.setCacheSystem(cacheSystem);

        AbstractUrlBasedView one = (AbstractUrlBasedView) underTest.resolveViewName("one", Locale.ENGLISH);
        assertThat(one, instanceOf(JtwigView.class));
        assertEquals("one", one.getUrl());

        one.setApplicationContext(applicationContext);
        one.setServletContext(servletContext);
        one.afterPropertiesSet();

        one.render(model, request, response);
        verify(env).load(eq("one"));
        verify(cache).getParsed(eq("one"));
//        verify(cacheSystem).get(eq("one"), Matchers.any(Callable.class));
//        verify(cache).getCompiled(eq("one"));
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

        verify(env.getFunctionRepository()).include(this);
    }
}
