package org.jtwig.functions.repository.api;

import org.jtwig.functions.parameters.input.InputParameters;
import org.jtwig.functions.repository.model.Function;

import java.util.Collection;

public interface FunctionRepository {
    FunctionRepository include (Object instance);
    FunctionRepository add (String name, Function function);
    FunctionRepository aliases(String name, String[] aliases);

    boolean containsFunctionName(String name);

    Collection<Function> retrieve(String name, InputParameters parameters);
}
