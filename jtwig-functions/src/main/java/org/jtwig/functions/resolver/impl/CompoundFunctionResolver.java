package org.jtwig.functions.resolver.impl;

import com.google.common.base.Optional;
import org.jtwig.functions.parameters.input.InputParameters;
import org.jtwig.functions.resolver.api.FunctionResolver;
import org.jtwig.functions.resolver.model.Executable;

import java.util.ArrayList;
import java.util.Collection;

public class CompoundFunctionResolver implements FunctionResolver {
    private Collection<FunctionResolver> functionResolvers = new ArrayList<>();

    public CompoundFunctionResolver withResolver (FunctionResolver resolver) {
        this.functionResolvers.add(resolver);
        return this;
    }

    @Override
    public Optional<Executable> resolve(String name, InputParameters parameters) {
        for (FunctionResolver functionResolver : functionResolvers) {
            Optional<Executable> resolve = functionResolver.resolve(name, parameters);
            if (resolve.isPresent())
                return resolve;
        }
        return Optional.absent();
    }
}
