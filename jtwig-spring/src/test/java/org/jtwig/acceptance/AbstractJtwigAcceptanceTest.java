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

package org.jtwig.acceptance;

import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.jtwig.Environment;
import org.jtwig.extension.spring.SpringExtension;
import org.jtwig.configuration.JtwigConfigurationBuilder;
import org.jtwig.loader.impl.ClasspathLoader;
import org.jtwig.mvc.JtwigViewResolver;
import org.junit.After;
import org.junit.Before;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.theme.FixedThemeResolver;

import java.io.IOException;
import org.jtwig.configuration.JtwigConfiguration;
import org.jtwig.extension.core.CoreJtwigExtension;

public abstract class AbstractJtwigAcceptanceTest {
    private HttpClient httpClient = httpClient();

    private Server jetty;

    private GetMethod getResult;
    private void startServer () throws Exception {
        jetty = new Server(9090);

        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(ConfigClass.class, getClass());
        applicationContext.register(configurationClasses());

        ServletHolder servletHolder = new ServletHolder(new DispatcherServlet(applicationContext));
        ServletContextHandler context = new ServletContextHandler();

        context.setErrorHandler(null); // use Spring exception handler(s)
        context.setContextPath("/");
        context.setBaseResource(Resource.newResource(new ClassPathResource("").getURI().toString()));
        context.setSessionHandler(new SessionHandler());
        context.addServlet(servletHolder, "/");

        jetty.setHandler(context);
        jetty.start();
    }

    @After
    public void shutdown () throws Exception {
        jetty.stop();
        jetty.join();
    }

    @Before
    public void setUp () throws Exception {
        startServer();
    }



    protected HttpClient httpClient() {
        return new HttpClient();
    }

    protected int thePort() {
        return ((ServerConnector) jetty.getConnectors()[0]).getLocalPort();
    }

    protected GetMethod serverReceivesGetRequest(String relativeUrl) throws IOException {
        getResult = new GetMethod("http://localhost:"+thePort()+ relativeUrl);
        httpClient.executeMethod(getResult);

        getResult.getResponseBodyAsString();
        return getResult;
    }

    protected final GetMethod theGetResult() {
        return getResult;
    }

    protected Class<?>[] configurationClasses () {
        return new Class[]{
                JtwigViewResolverConfig.class
        };
    }

    @Configuration
    public static class JtwigViewResolverConfig {
        @Bean
        public ThemeResolver themeResolver () {
            FixedThemeResolver fixedThemeResolver = new FixedThemeResolver();
            fixedThemeResolver.setDefaultThemeName("default");
            return fixedThemeResolver;
        }

        @Bean
        public ViewResolver viewResolver () {
            JtwigViewResolver jtwigViewResolver = new JtwigViewResolver(environment());
            jtwigViewResolver.setPrefix("/WEB-INF/views/");
            jtwigViewResolver.setSuffix(".twig.html");
            jtwigViewResolver.setThemeResolver(themeResolver());
            jtwigViewResolver.setUseThemeInViewPath(true);
            return jtwigViewResolver;
        }
        
        @Bean
        public Environment environment (/*ServletContext ctx*/) {
            ClasspathLoader classpath = new ClasspathLoader();
//            ChainLoader chainLoader = new ChainLoader(Arrays.asList(web, classpath));
            
            JtwigConfiguration config = JtwigConfigurationBuilder.newConfiguration()
                    .withLoader(classpath)
                    .build();
            config.getExtensions().addExtension(new CoreJtwigExtension(config));
            config.getExtensions().addExtension(springExtension());
            return new Environment(config);
        }
        
        @Bean
        public SpringExtension springExtension () {
            return new SpringExtension();
        }
    }

    @Configuration
    @EnableWebMvc
    public static class ConfigClass extends WebMvcConfigurerAdapter {

    }
}
