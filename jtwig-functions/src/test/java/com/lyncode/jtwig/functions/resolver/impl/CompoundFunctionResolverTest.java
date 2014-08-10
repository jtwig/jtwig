package com.lyncode.jtwig.functions.resolver.impl;

import com.google.common.base.Optional;
import com.lyncode.jtwig.functions.parameters.input.InputParameters;
import com.lyncode.jtwig.functions.resolver.api.FunctionResolver;
import com.lyncode.jtwig.functions.resolver.model.Executable;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class CompoundFunctionResolverTest {
    private final FunctionResolver firstResolver = mock(FunctionResolver.class);
    private final FunctionResolver secondResolver = mock(FunctionResolver.class);
    private FunctionResolver underTest = new CompoundFunctionResolver()
            .withResolver(firstResolver)
            .withResolver(secondResolver);

    @Test
    public void ifDelegatesDoNotResolve() throws Exception {
        InputParameters parameters = InputParameters.parameters("test");
        when(firstResolver.resolve("name", parameters)).thenReturn(Optional.<Executable>absent());
        when(secondResolver.resolve("name", parameters)).thenReturn(Optional.<Executable>absent());

        assertFalse(underTest.resolve("name", parameters).isPresent());
    }

    @Test
    public void ifSecondFinds() throws Exception {
        Executable result = mock(Executable.class);
        InputParameters parameters = InputParameters.parameters("test");
        when(firstResolver.resolve("name", parameters)).thenReturn(Optional.<Executable>absent());
        when(secondResolver.resolve("name", parameters)).thenReturn(Optional.of(result));

        assertTrue(underTest.resolve("name", parameters).isPresent());
    }

    @Test
    public void ifFirstFinds() throws Exception {
        Executable result = mock(Executable.class);
        InputParameters parameters = InputParameters.parameters("test");
        when(firstResolver.resolve("name", parameters)).thenReturn(Optional.of(result));
        when(secondResolver.resolve("name", parameters)).thenReturn(Optional.<Executable>absent());

        assertTrue(underTest.resolve("name", parameters).isPresent());
        verifyZeroInteractions(secondResolver);
    }
}