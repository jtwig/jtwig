package com.lyncode.jtwig.functions.resolver.api;

import com.google.common.base.Optional;
import com.lyncode.jtwig.functions.parameters.input.InputParameters;
import com.lyncode.jtwig.functions.resolver.model.Executable;

public interface FunctionResolver {
    Optional<Executable> resolve (String name, InputParameters parameters);
}
