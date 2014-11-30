package org.jtwig.unit.services.impl.url;

import org.jtwig.services.impl.url.ThemedResourceUrlResolver;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ThemedResourceUrlResolverTest {
    private ThemedResourceUrlResolver underTest = new ThemedResourceUrlResolver("theme");

    @Test
    public void startingWithSlash() throws Exception {
        assertThat(underTest.resolve("prefix", "/test", ".html"), equalTo("prefix/theme/test.html"));
    }
    @Test
    public void startingWithoutSlash() throws Exception {
        assertThat(underTest.resolve("prefix/", "test", ".html"), equalTo("prefix/theme/test.html"));
    }
}
