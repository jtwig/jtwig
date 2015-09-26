package org.jtwig.acceptance.functions;

import org.jtwig.acceptance.AbstractViewAcceptanceTest;
import org.junit.Test;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import org.jtwig.extension.spring.SpringExtension;
import static org.jtwig.util.matchers.ExceptionMatcherBuilder.exception;
import static org.junit.Assert.fail;

public class TranslateViewTest extends AbstractViewAcceptanceTest {
    
    @Test
    public void translateWithoutLocaleResolver() throws Exception {
        registerExtensions();
        try {
            renderString("{{ 'one'|translate }}");
            fail();
        } catch (Exception e) {
            assertThat(e, exception().withCause(exception().withMessage(containsString("a bean of type org.springframework.web.servlet.LocaleResolver must be configured"))));
        }
    }

    @Test
    public void undefinedMessage() throws Exception {
        registerBean("localeResolver", new FixedLocaleResolver());
        registerExtensions();

        try {
            renderString("{{ 'one'|translate }}");
            fail();
        } catch (Exception e) {
            assertThat(e, exception().withCause(exception().ofType(NoSuchMessageException.class)));
        }
    }

    protected void registerExtensions() {
        SpringExtension springExtension = new SpringExtension();
        applicationContext().getAutowireCapableBeanFactory().autowireBean(springExtension);
        env.getConfiguration().getExtensions().addExtension(springExtension);
    }

}
