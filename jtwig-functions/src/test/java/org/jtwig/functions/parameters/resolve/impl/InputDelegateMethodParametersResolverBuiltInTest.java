package org.jtwig.functions.parameters.resolve.impl;

import org.jtwig.functions.parameters.convert.DemultiplexerConverter;
import org.jtwig.functions.parameters.input.InputParameters;
import org.jtwig.functions.parameters.resolve.api.InputParameterResolverFactory;
import org.jtwig.functions.parameters.resolve.api.MethodParametersResolver;
import org.jtwig.functions.parameters.resolve.api.ParameterResolver;
import org.jtwig.functions.parameters.resolve.model.ResolvedParameters;
import org.junit.Test;

import java.lang.reflect.Method;

import static java.util.Arrays.asList;
import java.util.List;
import org.jtwig.functions.annotations.JtwigFunction;
import org.jtwig.functions.annotations.Parameter;
import static org.jtwig.functions.parameters.input.InputParameters.parameters;
import static org.junit.Assert.assertTrue;

public class InputDelegateMethodParametersResolverBuiltInTest {
    private InputParameterResolverFactory resolverFactory = new InputParameterResolverFactory() {
        @Override
        public ParameterResolver create(InputParameters parameters) {
            return new ParameterAnnotationParameterResolver(parameters, new DemultiplexerConverter());
        }
    };
    private MethodParametersResolver underTest = new InputDelegateMethodParametersResolver(resolverFactory);

    @Test
    public void canResolveParameters() throws Exception {
        ResolvedParameters resolve = underTest.resolve(new ResolvedParameters(getFunction()), parameters(asList(1, 2, 3, 4), 3));
        assertTrue(resolve.isFullyResolved());
    }

    private Method getFunction() {
        try {
            return InputDelegateMethodParametersResolverBuiltInTest.class.getDeclaredMethod("testFunction", List.class, Integer.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    
    @JtwigFunction(name = "testFunction")
    public Object testFunction(@Parameter List obj, @Parameter Integer i) {
        return null;
    }
}
