package org.jtwig.cache.impl;

import org.jtwig.cache.JtwigTemplateCacheSystem;
import org.jtwig.content.api.Renderable;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.Callable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NoCacheSystemTest {
    private JtwigTemplateCacheSystem underTest;

    @Test
    public void delegateRetrieveToCallable() throws Exception {
        Callable provider = mock(Callable.class);
        Renderable renderable = mock(Renderable.class);
        when(provider.call()).thenReturn(renderable);
        underTest = new NoCacheSystem();

        Renderable result = underTest.get("test", provider);

        assertThat(result, is(equalTo(renderable)));
    }

    @Test
    public void callableExceptionShouldBubbleUpAsIllegalState() throws Exception {
        Callable provider = mock(Callable.class);
        when(provider.call()).thenThrow(new IOException());
        underTest = new NoCacheSystem();

        try {
            underTest.get("test", provider);
        } catch (Exception e) {
            assertThat(e, instanceOf(IllegalStateException.class));
        }

    }
}