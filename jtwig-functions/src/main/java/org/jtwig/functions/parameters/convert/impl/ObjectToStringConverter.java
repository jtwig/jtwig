package org.jtwig.functions.parameters.convert.impl;

import com.google.common.base.Optional;
import org.jtwig.functions.parameters.convert.api.ParameterConverter;

public class ObjectToStringConverter implements ParameterConverter {
    @Override
    public Optional convert(Object input) {
        return Optional.of(input.toString());
    }
}
