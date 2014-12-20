package org.jtwig.unit.render.config;

import org.jtwig.cache.impl.ExecutionCache;
import org.jtwig.render.config.RenderConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RenderConfigurationTest {
    private RenderConfiguration underTest = new RenderConfiguration(new ExecutionCache());

    @Test
    public void logNonStrictMode() throws Exception {
        assertTrue(underTest.logNonStrictMode(true)
                .logNonStrictMode());
    }
}
