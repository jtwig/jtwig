package com.lyncode.jtwig.functions.parameters.convert;

import com.google.common.base.Optional;
import com.lyncode.jtwig.functions.parameters.convert.api.ParameterConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Optional.absent;
import static java.util.Collections.synchronizedList;

public class DemultiplexerConverter {
    private Map<Class, Collection<ParameterConverter>> list = new ConcurrentHashMap<>();

    public DemultiplexerConverter withConverter (Class to, ParameterConverter converter) {
        if (!list.containsKey(to))
            list.put(to, synchronizedList(new ArrayList<ParameterConverter>()));
        list.get(to).add(converter);
        return this;
    }

    public Optional convert(Object input, Class to) {
        Collection<ParameterConverter> parameterConverters = list.get(to);
        if (parameterConverters != null) {
            for (ParameterConverter parameterConverter : parameterConverters) {
                Optional convert = parameterConverter.convert(input);
                if (convert.isPresent())
                    return convert;
            }
        }
        return absent();
    }
}
