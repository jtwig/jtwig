package org.jtwig.functions.parameters.resolve.api;

import org.jtwig.functions.parameters.input.InputParameters;

public interface InputParameterResolverFactory {
    ParameterResolver create (InputParameters parameters);
}
