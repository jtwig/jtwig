package org.jtwig.unit.cache.impl;

import org.jtwig.cache.impl.PersistentTemplateCacheSystem;
import org.jtwig.content.api.Renderable;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.Callable;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
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

    @Test
    public void callerExecutionException() throws Exception {
        Callable provider = mock(Callable.class);
        when(provider.call()).thenThrow(new IOException());

        try {
            underTest.get("one", provider);
            fail();
        } catch (Exception e) {
            assertThat(e, instanceOf(IllegalStateException.class));
        }
    }
}
