package com.lyncode.jtwig.functions.parameters.resolve.impl;

import com.google.common.base.Optional;
import com.lyncode.jtwig.functions.parameters.input.InputParameters;
import com.lyncode.jtwig.functions.parameters.resolve.api.InputParameterResolverFactory;
import com.lyncode.jtwig.functions.parameters.resolve.api.MethodParametersResolver;
import com.lyncode.jtwig.functions.parameters.resolve.api.ParameterResolver;
import com.lyncode.jtwig.functions.parameters.resolve.model.ResolvedParameters;
import com.lyncode.jtwig.functions.reflection.JavaMethodParameter;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.lang.reflect.Method;

import static com.lyncode.jtwig.functions.parameters.resolve.api.ParameterResolver.Value;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InputDelegateMethodParametersResolverTest {
    private InputParameterResolverFactory resolverFactory = mock(InputParameterResolverFactory.class);
    private MethodParametersResolver underTest = new InputDelegateMethodParametersResolver(resolverFactory);
    private ResolvedParameters resolvedParameters = new ResolvedParameters(exampleMethod());

    @Test
    public void resolverNotApplicable() throws Exception {
        InputParameters parameters = InputParameters.parameters();
        ParameterResolver parameterResolver = mock(ParameterResolver.class);
        when(resolverFactory.create(parameters)).thenReturn(parameterResolver);
        when(parameterResolver.resolve(argThat(withPosition(equalTo(0))))).thenReturn(Optional.<Value>absent());

        ResolvedParameters result = underTest.resolve(resolvedParameters, parameters);
        assertThat(result.unresolvedParameters(), hasSize(1));
    }

    @Test
    public void resolverApplicable() throws Exception {
        InputParameters parameters = InputParameters.parameters();
        ParameterResolver parameterResolver = mock(ParameterResolver.class);
        when(resolverFactory.create(parameters)).thenReturn(parameterResolver);
        when(parameterResolver.resolve(argThat(withPosition(equalTo(0))))).thenReturn(Optional.of(new Value(null)));

        ResolvedParameters result = underTest.resolve(resolvedParameters, parameters);

        assertThat(result.unresolvedParameters(), empty());
    }

    public String example (String parameter) {
        return parameter;
    }

    private Method exampleMethod() {
        try {
            return this.getClass().getDeclaredMethod("example", String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Matcher<JavaMethodParameter> withPosition(Matcher<Integer> matcher) {
        return new FeatureMatcher<JavaMethodParameter, Integer>(matcher, "position", "") {
            @Override
            protected Integer featureValueOf(JavaMethodParameter actual) {
                return actual.position();
            }
        };
    }
}