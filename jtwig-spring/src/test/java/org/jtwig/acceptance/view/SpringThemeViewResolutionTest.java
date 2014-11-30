package org.jtwig.acceptance.view;

import org.jtwig.acceptance.AbstractJtwigAcceptanceTest;
import org.jtwig.mvc.JtwigViewResolver;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.theme.FixedThemeResolver;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.jtwig.util.SyntacticSugar.given;
import static org.jtwig.util.SyntacticSugar.then;
import static org.jtwig.util.matchers.GetMethodMatchers.body;

@Controller
public class SpringThemeViewResolutionTest extends AbstractJtwigAcceptanceTest {
    @Test
    public void resolvesThemedView() throws Exception {
        given(serverReceivesGetRequest("/"));
        then(theGetResult(), body(equalTo("ok")));
    }

    @RequestMapping("/")
    public String action () {
        return "view/test";
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
            JtwigViewResolver jtwigViewResolver = new JtwigViewResolver();
            jtwigViewResolver.setPrefix("/WEB-INF/views/");
            jtwigViewResolver.setSuffix(".twig.html");
            jtwigViewResolver.setUseThemeInViewPath(true);
            jtwigViewResolver.setThemeResolver(themeResolver());
            return jtwigViewResolver;
        }
    }
}
