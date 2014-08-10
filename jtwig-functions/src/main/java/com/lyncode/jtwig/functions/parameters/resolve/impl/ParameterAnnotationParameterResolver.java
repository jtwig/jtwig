package com.lyncode.jtwig.functions.parameters.resolve.impl;

import com.google.common.base.Optional;
import com.lyncode.jtwig.functions.annotations.Parameter;
import com.lyncode.jtwig.functions.parameters.convert.DemultiplexerConverter;
import com.lyncode.jtwig.functions.parameters.input.InputParameters;
import com.lyncode.jtwig.functions.parameters.resolve.api.ParameterResolver;
import com.lyncode.jtwig.functions.reflection.JavaMethodParameter;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;

public class ParameterAnnotationParameterResolver implements ParameterResolver {
    private final InputParameters input;
    private final DemultiplexerConverter converter;

    public ParameterAnnotationParameterResolver(InputParameters input, DemultiplexerConverter converter) {
        this.input = input;
        this.converter = converter;
    }

    @Override
    public Optional<Value> resolve(JavaMethodParameter parameter) {
        if (parameter.hasAnnotation(Parameter.class)) {
            if (parameter.isVarArg()) {
                // try to resolve the rest of parameters to one array
                Class<?> componentType = parameter.type().getComponentType();
                List<Object> args = new ArrayList<>();
                for (int i=parameter.annotationIndex(Parameter.class); i<input.length(); i++) {
                    Object value = input.valueAt(i);
                    if (value == null) args.add(value);
                    else {
                        if (ClassUtils.isAssignable(value.getClass(), componentType))
                            args.add(value);
                        else {
                            Optional convert = converter.convert(value, componentType);
                            if (convert.isPresent())
                                args.add(convert.get());
                            else
                                return Optional.absent();
                        }
                    }
                }
                Object newInstance = Array.newInstance(componentType, args.size());
                for (int i = 0;i<args.size();i++) {
                    Array.set(newInstance, i, args.get(i));
                }
                return Optional.of(new Value(newInstance));
            } else {
                int index = parameter.annotationIndex(Parameter.class);
                Object value = input.valueAt(index);
                if (value == null) return of(new Value(null));
                if (ClassUtils.isAssignable(value.getClass(), parameter.type())) {
                    return of(new Value(value));
                } else {
                    Optional convert = converter.convert(value, parameter.type());
                    if (convert.isPresent())
                        return of(new Value(convert.get()));
                }
            }
        }
        return absent();
    }

}
