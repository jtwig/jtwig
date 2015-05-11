package org.jtwig.functions.parameters.resolve.impl;

import com.google.common.base.Optional;
import org.jtwig.functions.parameters.input.InputParameters;
import org.jtwig.functions.parameters.resolve.api.InputParameterResolverFactory;
import org.jtwig.functions.parameters.resolve.api.MethodParametersResolver;
import org.jtwig.functions.parameters.resolve.api.ParameterResolver;
import org.jtwig.functions.parameters.resolve.model.ResolvedParameters;
import org.jtwig.functions.reflection.JavaMethodParameter;

import java.util.Collection;

public class InputDelegateMethodParametersResolver implements MethodParametersResolver {
    private final InputParameterResolverFactory factory;

    public InputDelegateMethodParametersResolver(InputParameterResolverFactory factory) {
        this.factory = factory;
    }

    @Override
    public ResolvedParameters resolve(ResolvedParameters parameters, InputParameters inputParameters) {
        Collection<JavaMethodParameter> unresolvedParameters = parameters.unresolvedParameters();
        for (JavaMethodParameter unresolvedParameter : unresolvedParameters) {

            Optional<ParameterResolver.Value> resolved = factory
                    .create(inputParameters)
                    .resolve(unresolvedParameter);

            if (resolved.isPresent())
                parameters.resolve(unresolvedParameter, resolved.get().value());
        }
        return parameters;
    }
}
