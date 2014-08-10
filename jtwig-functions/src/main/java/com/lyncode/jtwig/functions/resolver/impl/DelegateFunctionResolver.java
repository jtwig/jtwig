package com.lyncode.jtwig.functions.resolver.impl;

import com.google.common.base.Optional;
import com.lyncode.jtwig.functions.parameters.input.InputParameters;
import com.lyncode.jtwig.functions.parameters.resolve.api.MethodParametersResolver;
import com.lyncode.jtwig.functions.parameters.resolve.model.ResolvedParameters;
import com.lyncode.jtwig.functions.repository.api.FunctionRepository;
import com.lyncode.jtwig.functions.repository.model.Function;
import com.lyncode.jtwig.functions.resolver.api.FunctionResolver;
import com.lyncode.jtwig.functions.resolver.model.Executable;

import java.util.Collection;

import static com.google.common.base.Optional.absent;

public class DelegateFunctionResolver implements FunctionResolver {
    private final FunctionRepository repository;
    private final MethodParametersResolver parametersResolver;

    public DelegateFunctionResolver(FunctionRepository repository, MethodParametersResolver parametersResolver) {
        this.repository = repository;
        this.parametersResolver = parametersResolver;
    }

    @Override
    public Optional<Executable> resolve(String name, InputParameters parameters) {
        Collection<Function> functions = repository.retrieve(name, parameters);
        for (Function function : functions) {
            ResolvedParameters resolvedParameters = new ResolvedParameters(function.method());
            resolvedParameters = parametersResolver.resolve(resolvedParameters, parameters);
            if (resolvedParameters.isFullyResolved())
                return Optional.of(new Executable(function, resolvedParameters.values()));
        }
        return absent();
    }
}
