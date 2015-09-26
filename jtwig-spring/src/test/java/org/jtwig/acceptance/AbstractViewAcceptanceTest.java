package org.jtwig.acceptance;

import java.util.Locale;
import org.jtwig.Environment;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.configuration.JtwigConfigurationBuilder;
import org.jtwig.loader.impl.StringLoader;
import org.jtwig.mvc.JtwigViewResolver;
import org.junit.Before;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.theme.FixedThemeResolver;

public abstract class AbstractViewAcceptanceTest {
    private JtwigViewResolver viewResolver;
    private AnnotationConfigWebApplicationContext applicationContext;
    private MockHttpServletRequest httpServletRequest;
    protected JtwigConfiguration config;
    protected Environment env;

    @Before
    public void setUp() throws Exception {
        config = spy(JtwigConfigurationBuilder.defaultConfiguration());
        env = spy(new Environment(config));
        
        MockServletContext servletContext = new MockServletContext();
        httpServletRequest = new MockHttpServletRequest();

        applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.setServletContext(servletContext);
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));
        applicationContext.refresh();

        viewResolver = new JtwigViewResolver(env);
        applicationContext.getBeanFactory().registerSingleton("viewResolver", viewResolver);
        viewResolver.setApplicationContext(applicationContext);
    }
    
    protected ApplicationContext applicationContext() {
        return applicationContext;
    }

    protected void registerBean (String name, Object instance) {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(instance);
        applicationContext.getBeanFactory().registerSingleton(name, instance);
    }

    protected void withDefaultThemeResolver () {
        FixedThemeResolver themeResolver = new FixedThemeResolver();
        themeResolver.setDefaultThemeName("default");
        registerBean("themeResolver", themeResolver);
        viewResolver.setThemeResolver(themeResolver);
    }

    public String render (String location, ModelMap modelMap) throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        viewResolver.resolveViewName(location, Locale.getDefault())
                .render(modelMap, httpServletRequest, response);

        return response.getContentAsString();
    }

    public String renderString (final String inlineTemplate, ModelMap modelMap) throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(viewResolver.getEnvironment().getConfiguration().getLoader()).thenReturn(new StringLoader(inlineTemplate));
        viewResolver.resolveViewName("", Locale.getDefault())
                .render(modelMap, new MockHttpServletRequest(), response);

        return response.getContentAsString();
    }

    public String renderString (final String inlineTemplate) throws Exception {
        return renderString(inlineTemplate, new ModelMap());
    }

    protected JtwigViewResolver viewResolver() {
        return viewResolver;
    }
}
