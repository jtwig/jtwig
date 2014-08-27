package com.lyncode.jtwig.services.impl.url;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class IdentityUrlResolverTest {
    @Test
    public void outputsTheInput() throws Exception {
        assertThat(IdentityUrlResolver.INSTANCE.resolve("","/test", ""), equalTo("/test"));
    }
}