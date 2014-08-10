package com.lyncode.jtwig.functions.reflection;

import java.lang.reflect.Method;

public class JavaMethod {
    private final Method method;
    private final JavaMethodParameters parameters;

    public JavaMethod(Method method) {
        this.method = method;
        this.parameters = new JavaMethodParameters(method);
    }

    public JavaMethodParameters parameters() {
        return parameters;
    }
}
