package org.jtwig.functions.resolver.impl;

import com.google.common.base.Optional;
import org.jtwig.functions.parameters.input.InputParameters;
import org.jtwig.functions.parameters.resolve.api.MethodParametersResolver;
import org.jtwig.functions.parameters.resolve.model.ResolvedParameters;
import org.jtwig.functions.repository.api.FunctionRepository;
import org.jtwig.functions.repository.model.Function;
import org.jtwig.functions.resolver.api.FunctionResolver;
import org.jtwig.functions.resolver.model.Executable;

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
