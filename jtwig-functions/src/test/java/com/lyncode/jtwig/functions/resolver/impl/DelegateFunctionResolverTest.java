package com.lyncode.jtwig.functions.resolver.impl;

import com.lyncode.jtwig.functions.parameters.input.InputParameters;
import com.lyncode.jtwig.functions.parameters.resolve.api.MethodParametersResolver;
import com.lyncode.jtwig.functions.parameters.resolve.model.ResolvedParameters;
import com.lyncode.jtwig.functions.reflection.JavaMethodParameter;
import com.lyncode.jtwig.functions.repository.api.FunctionRepository;
import com.lyncode.jtwig.functions.repository.model.Function;
import com.lyncode.jtwig.functions.resolver.api.FunctionResolver;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Collection;

import static com.lyncode.jtwig.functions.parameters.input.InputParameters.parameters;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DelegateFunctionResolverTest {
    private FunctionRepository repository = mock(FunctionRepository.class);
    private MethodParametersResolver methodParametersResolver = mock(MethodParametersResolver.class);
    private FunctionResolver underTest = new DelegateFunctionResolver(repository, methodParametersResolver);

    @Test
    public void nonExistingFunction() throws Exception {
        assertFalse(underTest.resolve("test", parameters()).isPresent());
    }

    @Test
    public void functionExistsButResolverUncapableOfResolving() throws Exception {
        Function function = mock(Function.class);
        when(function.method()).thenReturn(exampleMethod());
        InputParameters inputParameters = parameters("hello");
        when(repository.retrieve("test", inputParameters)).thenReturn(asList(function));
        when(methodParametersResolver.resolve(any(ResolvedParameters.class), eq(inputParameters)))
                .thenReturn(resolvedParameters(exampleMethod()));


        assertFalse(underTest.resolve("test", inputParameters).isPresent());
    }

    @Test
    public void functionExistsButResolverCapableOfResolving() throws Exception {
        Function function = mock(Function.class);
        when(function.method()).thenReturn(exampleMethod());
        InputParameters inputParameters = parameters("hello");
        when(repository.retrieve("test", inputParameters)).thenReturn(asList(function));
        when(methodParametersResolver.resolve(any(ResolvedParameters.class), eq(inputParameters)))
                .thenReturn(resolvedParameters(exampleMethod(), "hello"));

        assertTrue(underTest.resolve("test", inputParameters).isPresent());
    }

    @Test
    public void moreInputParametersThanFunctionRequiresNotAppliedForVarArgs() throws Exception {
        Function function = mock(Function.class);
        when(function.method()).thenReturn(varArgsMethod());
        InputParameters inputParameters = parameters("hello", "two");
        when(repository.retrieve("test", inputParameters)).thenReturn(asList(function));
        when(methodParametersResolver.resolve(any(ResolvedParameters.class), eq(inputParameters)))
                .thenReturn(resolvedParameters(exampleMethod(), (Object) new String[]{"one", "two"}));

        assertTrue(underTest.resolve("test", inputParameters).isPresent());
    }

    private ResolvedParameters resolvedParameters(Method method, Object... values) {
        ResolvedParameters resolvedParameters = new ResolvedParameters(method);
        int i = 0;
        Collection<JavaMethodParameter> parameters = resolvedParameters.unresolvedParameters();
        if (parameters.size() != values.length)
            return resolvedParameters;
        for (JavaMethodParameter parameter : parameters)
            resolvedParameters.resolve(parameter, values[i++]);
        return resolvedParameters;
    }

    private Method exampleMethod() {
        try {
            return this.getClass().getDeclaredMethod("example", String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Method varArgsMethod() {
        try {
            return this.getClass().getDeclaredMethod("exampleVarArgs", String[].class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public String example (String value) {
        return value;
    }

    public String exampleVarArgs (String... value) {
        return "one";
    }
}