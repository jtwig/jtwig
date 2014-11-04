package com.lyncode.jtwig.unit.cache.impl;

import com.lyncode.jtwig.cache.impl.PersistentTemplateCacheSystem;
import com.lyncode.jtwig.content.api.Renderable;
import org.junit.Test;

import java.util.concurrent.Callable;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class PersistentTemplateCacheSystemTest {
    private PersistentTemplateCacheSystem underTest = new PersistentTemplateCacheSystem();
    private Renderable renderable = mock(Renderable.class);

    @Test
    public void cachedValueReturnsInitialisedValue() throws Exception {
        Renderable cached = underTest.get("key", new Callable<Renderable>() {
            @Override
            public Renderable call() throws Exception {
                return renderable;
            }
        });

        assertThat(cached, equalTo(renderable));

    }
    @Test
    public void callbackIsExecutedOnlyOnce() throws Exception {
        Callable provider = mock(Callable.class);
        when(provider.call()).thenReturn(renderable);

        underTest.get("key", provider);
        underTest.get("key", provider);

        verify(provider, times(1)).call();
    }
}