package org.jtwig.functions.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class JavaMethodParameter {
    private final int position;
    private final Method method;
    private boolean varArg;

    public JavaMethodParameter(int position, Method method) {
        this.position = position;
        this.method = method;
    }


    public boolean hasAnnotation(Class<?> parameterClass) {
        Annotation[] annotations = method.getParameterAnnotations()[position];
        return containsAnnotation(parameterClass, annotations);
    }

    public Class<?> type() {
        return method.getParameterTypes()[position];
    }

    public int annotationIndex(Class<?> annotationClass) {
        int count = 0;
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < position; i++) {
            if (containsAnnotation(annotationClass, parameterAnnotations[i]))
                count++;
        }
        return count;
    }

    private boolean containsAnnotation(Class<?> parameterClass, Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(parameterClass))
                return true;
        }
        return false;
    }

    public int position() {
        return position;
    }

    public boolean isVarArg() {
        return method.isVarArgs() && position == (method.getParameterTypes().length - 1);
    }
}
