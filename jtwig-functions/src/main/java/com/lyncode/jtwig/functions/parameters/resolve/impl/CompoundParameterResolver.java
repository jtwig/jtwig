package com.lyncode.jtwig.functions.parameters.resolve.impl;

import com.google.common.base.Optional;
import com.lyncode.jtwig.functions.parameters.resolve.api.ParameterResolver;
import com.lyncode.jtwig.functions.reflection.JavaMethodParameter;

import java.util.ArrayList;
import java.util.Collection;

public class CompoundParameterResolver implements ParameterResolver {
    private Collection<ParameterResolver> list = new ArrayList<>();

    public CompoundParameterResolver withResolver (ParameterResolver resolver) {
        list.add(resolver);
        return this;
    }

    @Override
    public Optional<Value> resolve(JavaMethodParameter parameter) {
        for (ParameterResolver parameterResolver : list) {
            Optional<Value> resolve = parameterResolver.resolve(parameter);
            if (resolve.isPresent())
                return resolve;
        }
        return Optional.absent();
    }
}
