package org.jtwig.functions.resolver.api;

import com.google.common.base.Optional;
import org.jtwig.functions.parameters.input.InputParameters;
import org.jtwig.functions.resolver.model.Executable;

public interface FunctionResolver {
    Optional<Executable> resolve (String name, InputParameters parameters);
}
