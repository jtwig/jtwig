package com.lyncode.jtwig.functions.parameters.resolve.api;

import com.lyncode.jtwig.functions.parameters.input.InputParameters;
import com.lyncode.jtwig.functions.parameters.resolve.model.ResolvedParameters;

public interface MethodParametersResolver {
    ResolvedParameters resolve (ResolvedParameters parameters, InputParameters input);
}
