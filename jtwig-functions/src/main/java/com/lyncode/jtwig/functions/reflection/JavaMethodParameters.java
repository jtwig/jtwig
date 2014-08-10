package com.lyncode.jtwig.functions.reflection;

import com.lyncode.jtwig.functions.annotations.Parameter;

import java.lang.reflect.Method;
import java.util.Collection;

public class JavaMethodParameters {
    private final Method method;

    public JavaMethodParameters(Method method) {
        this.method = method;
    }

    public Collection<JavaMethodParameter> selectByAnnotation(Class<Parameter> parameterClass) {
        return null;
    }
}
