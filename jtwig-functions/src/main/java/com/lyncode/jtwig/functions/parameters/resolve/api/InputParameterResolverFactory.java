package com.lyncode.jtwig.functions.parameters.resolve.api;

import com.lyncode.jtwig.functions.parameters.input.InputParameters;

public interface InputParameterResolverFactory {
    ParameterResolver create (InputParameters parameters);
}
