package com.lyncode.jtwig.functions.parameters.resolve.impl;

import com.google.common.base.Optional;
import com.lyncode.jtwig.functions.parameters.resolve.api.ParameterResolver;
import com.lyncode.jtwig.functions.reflection.JavaMethodParameter;
import org.junit.Test;

import static com.lyncode.jtwig.functions.parameters.resolve.api.ParameterResolver.Value;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class CompoundParameterResolverTest {
    private ParameterResolver firstResolver = mock(ParameterResolver.class);
    private ParameterResolver secondResolver = mock(ParameterResolver.class);
    private ParameterResolver parameterResolver = new CompoundParameterResolver()
            .withResolver(firstResolver)
            .withResolver(secondResolver);

    @Test
    public void ifNoDelegatesResolve() throws Exception {
        JavaMethodParameter input = mock(JavaMethodParameter.class);

        given(firstResolver.resolve(input)).willReturn(Optional.<Value>absent());
        given(secondResolver.resolve(input)).willReturn(Optional.<Value>absent());

        assertFalse(parameterResolver.resolve(input).isPresent());
    }

    @Test
    public void ifFirstDelegateResolves() throws Exception {
        JavaMethodParameter input = mock(JavaMethodParameter.class);

        given(firstResolver.resolve(input)).willReturn(Optional.of(new Value(null)));
        given(secondResolver.resolve(input)).willReturn(Optional.<Value>absent());

        assertTrue(parameterResolver.resolve(input).isPresent());
        verifyZeroInteractions(secondResolver);
    }

    @Test
    public void ifSecondDelegateResolves() throws Exception {
        JavaMethodParameter input = mock(JavaMethodParameter.class);

        given(firstResolver.resolve(input)).willReturn(Optional.<Value>absent());
        given(secondResolver.resolve(input)).willReturn(Optional.of(new Value(null)));

        assertTrue(parameterResolver.resolve(input).isPresent());
    }
}