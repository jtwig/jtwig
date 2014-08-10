package com.lyncode.jtwig.functions.resolver.model;

import com.lyncode.jtwig.functions.repository.model.Function;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class ExecutableTest {
    private final Function function = mock(Function.class);
    private Executable underTest = new Executable(function, new Object[]{});

    @Test
    public void executeInvokesMethod() throws Exception {
        when(function.method()).thenReturn(exampleMethod());
        when(function.holder()).thenReturn(this);

        underTest.execute();
        verify(function).holder();
    }

    public Method exampleMethod() {
        try {
            return getClass().getDeclaredMethod("exampleMethod");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}