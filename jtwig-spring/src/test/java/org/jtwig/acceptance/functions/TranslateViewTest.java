package org.jtwig.acceptance.functions;

import org.jtwig.acceptance.AbstractViewAcceptanceTest;
import org.junit.Test;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.jtwig.util.matchers.ExceptionMatcherBuilder.exception;
import static org.junit.Assert.fail;

public class TranslateViewTest extends AbstractViewAcceptanceTest {
    @Test
    public void translateWithoutLocaleResolver() throws Exception {
        try {
            renderString("{{ translate('one') }}");
            fail();
        } catch (Exception e) {
            assertThat(e, exception().withCause(exception().withMessage(containsString("a bean of type org.springframework.web.servlet.LocaleResolver must be configured"))));
        }
    }

    @Test
    public void undefinedMessage() throws Exception {
        registerBean("localeResolver", new FixedLocaleResolver());

        try {
            renderString("{{ translate('one') }}");
            fail();
        } catch (Exception e) {
            assertThat(e, exception().withCause(exception().ofType(NoSuchMessageException.class)));
        }
    }

}
