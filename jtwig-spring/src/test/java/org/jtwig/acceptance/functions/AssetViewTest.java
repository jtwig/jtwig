package org.jtwig.acceptance.functions;

import org.jtwig.acceptance.AbstractViewAcceptanceTest;
import org.jtwig.exceptions.AssetResolveException;
import org.jtwig.extension.api.functions.FunctionException;
import org.jtwig.services.api.assets.AssetResolver;
import org.junit.Test;

import static org.jtwig.util.matchers.ExceptionMatcherBuilder.exception;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class AssetViewTest extends AbstractViewAcceptanceTest {
    @Test
    public void assetResolverExceptionShouldBubbleUp() throws Exception {
        registerBean("assetResolver", new AssetResolver() {
            @Override
            public String resolve(String asset) throws AssetResolveException {
                throw new AssetResolveException("Error");
            }
        });

        try {
            renderString("{{ asset('hello') }}");
            fail();
        } catch (Exception exception) {
            assertThat(exception, exception().withCause(exception().ofType(AssetResolveException.class)));
        }
    }

    @Test
    public void assetResolverUndefined() throws Exception {
        try {
            renderString("{{ asset('hello') }}");
            fail();
        } catch (Exception exception) {
            assertThat(exception, exception().withCause(exception().ofType(FunctionException.class)));
        }
    }
}
