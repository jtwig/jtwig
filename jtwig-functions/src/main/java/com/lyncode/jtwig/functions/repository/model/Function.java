package com.lyncode.jtwig.functions.repository.model;

import com.lyncode.jtwig.functions.parameters.resolve.model.MethodInformation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Function implements Comparable<Function> {
    public static FunctionBuilder functionFrom (Method method) {
        return new FunctionBuilder(method);
    }

    private final Object holder;
    private final Method method;
    private final MethodInformation methodInformation;

    private Function(Object holder, Method method) {
        this.holder = holder;
        this.method = method;
        this.methodInformation = new MethodInformation(method);
    }

    public Method method() {
        return method;
    }

    public Object holder() {
        return holder;
    }

    @Override
    public int compareTo(Function other) {
        if (methodInformation.numberOfArgs() > 0 && other.methodInformation.numberOfArgs() == this.methodInformation.numberOfArgs()) {
            int compareResult = 0;
            for (int i = 0;i< methodInformation.numberOfArgs();i++) {
                Class<?> type = this.methodInformation.getType(i);
                Class<?> otherType = other.methodInformation.getType(i);

                if (type.equals(String.class) && otherType.equals(Object.class)) {
                    compareResult--;
                    continue;
                } else if (type.equals(Object.class) && otherType.equals(String.class)) {
                    compareResult++;
                    continue;
                }


                if (type.equals(String.class))
                    type = Object.class;
                if (otherType.equals(String.class))
                    otherType = Object.class;

                if (type.equals(otherType))
                    continue;
                if (type.isAssignableFrom(otherType))
                    compareResult++;
                else if (otherType.isAssignableFrom(type))
                    compareResult--;
            }
            return compareResult;
        } else return Integer.compare(this.methodInformation.numberOfArgs(), other.methodInformation.numberOfArgs()) * -1;
    }


    public List<Class<?>> getParameterTypesWithAnnotation(Class<? extends Annotation> parameterClass) {
        List<Class<?>> result = new ArrayList<>();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0;i<parameterAnnotations.length;i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            for (Annotation annotation : parameterAnnotation) {
                if (annotation.annotationType().equals(parameterClass)) {
                    result.add(method.getParameterTypes()[i]);
                    break;
                }
            }
        }
        return result;
    }

    public static class FunctionBuilder {
        private Method method;

        public FunctionBuilder(Method method) {
            this.method = method;
        }

        public Function on (Object instance) {
            return new Function(instance, method);
        }
    }
}
