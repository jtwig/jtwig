package org.jtwig.functions.parameters.resolve.api;

import org.jtwig.functions.parameters.input.InputParameters;
import org.jtwig.functions.parameters.resolve.model.ResolvedParameters;

public interface MethodParametersResolver {
    ResolvedParameters resolve (ResolvedParameters parameters, InputParameters input);
}
